package org.sky.phoenix;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;
import org.sky.phoenix.domain.bo.LineageInfo;
import org.sky.phoenix.domain.config.JdbcConfig;
import org.sky.phoenix.domain.config.KafkaConfig;

import static org.sky.phoenix.common.SystemException.throwSystemException;
import static org.sky.phoenix.config.SystemConfig.*;
import static org.sky.phoenix.operator.source.SystemKafkaSource.createKafkaSource;
import static org.sky.phoenix.service.BusinessProcess.*;
import static org.sky.phoenix.service.BusinessSink.fieldLineageSink;
import static org.sky.phoenix.service.BusinessSink.rawLineageSink;


/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 10:41
 */
public class PhoenixApplication {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		config.set(RestOptions.BIND_PORT, "8081");
		try (StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)) {
			env.disableOperatorChaining();

			final OutputTag<String> errorJsonParseOutputTag = new OutputTag<>("error-json-parse-output", Types.STRING);
			KafkaConfig kafkaConfig = getSpecifiedKafkaConfig("phoenix");
			JdbcConfig jdbcConfig = getSpecifiedJdbcConfig("phoenix");

			SingleOutputStreamOperator<LineageInfo> ds = env.fromSource(createKafkaSource(kafkaConfig), WatermarkStrategy.noWatermarks(), "HiveLineageKafkaSource")
				.process(jsonParseProcess(errorJsonParseOutputTag))
				.name("jsonParseProcess");

			ds.process(rawLineageProcess())
				.name("rawLineageProcess")
				.addSink(rawLineageSink(jdbcConfig))
				.name("rawLineageSink");

			ds.process(fieldLineageProcess())
				.name("fieldLineageProcess")
				.addSink(fieldLineageSink(jdbcConfig))
				.name("fieldLineageSink");

			env.execute(APPLICATION_NAME);
		} catch (Exception e) {
			throw throwSystemException(PhoenixApplication.class, e);
		}
	}
}
