package org.sky.phoenix.domain.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 13:04
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KafkaConfig {

	private String bootstrapServers;
	private String topics;
	private String groupId;
	private String autoOffsetReset;
}
