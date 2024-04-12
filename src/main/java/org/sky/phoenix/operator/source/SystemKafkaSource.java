package org.sky.phoenix.operator.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.sky.phoenix.domain.config.KafkaConfig;

import static org.sky.phoenix.common.SystemException.throwSystemException;
import static org.sky.phoenix.common.enums.ErrorMessageEnum.PARAMETER_REQUIRED;
import static org.sky.phoenix.common.enums.OffsetEnum.EARLIEST;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 11:06
 */
@Slf4j
public class SystemKafkaSource {

	public static KafkaSource<String> createKafkaSource(KafkaConfig config) {
		if (config == null) {
			throw throwSystemException(SystemKafkaSource.class, PARAMETER_REQUIRED);
		}
		OffsetsInitializer startingOffsets = EARLIEST.name().equalsIgnoreCase(config.getAutoOffsetReset()) ? OffsetsInitializer.earliest() : OffsetsInitializer.latest();
		return KafkaSource.<String>builder()
			.setBootstrapServers(config.getBootstrapServers())
			.setTopics(config.getTopics())
			.setGroupId(config.getGroupId())
			.setStartingOffsets(startingOffsets)
			.setValueOnlyDeserializer(new SimpleStringSchema())
			.build();
	}
}
