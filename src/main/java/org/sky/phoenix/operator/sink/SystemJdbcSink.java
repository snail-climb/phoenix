package org.sky.phoenix.operator.sink;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.sky.phoenix.domain.config.JdbcConfig;
import org.sky.phoenix.operator.source.SystemKafkaSource;

import static org.sky.phoenix.common.SystemException.throwSystemException;
import static org.sky.phoenix.common.enums.ErrorMessageEnum.PARAMETER_REQUIRED;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 12:25
 */
public class SystemJdbcSink {

	public static <T> SinkFunction<T> createJdbcSink(JdbcConfig config, String sql, JdbcStatementBuilder<T> statementBuilder) {
		if (config == null) {
			throw throwSystemException(SystemKafkaSource.class, PARAMETER_REQUIRED);
		}
		JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
			.withBatchSize(config.getBatchSize())
			.withBatchIntervalMs(config.getBatchIntervalMs())
			.withMaxRetries(config.getMaxRetries())
			.build();
		JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
			.withDriverName(config.getDriverName())
			.withUrl(config.getUrl())
			.withUsername(config.getUsername())
			.withPassword(config.getPassword())
			.build();
		return JdbcSink.sink(sql, statementBuilder, executionOptions, connectionOptions);
	}
}
