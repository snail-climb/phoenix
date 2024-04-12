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
 * @since 2024-04-11 10:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JdbcConfig {

	private String driverName;
	private String url;
	private String username;
	private String password;
	private Integer batchSize;
	private Integer batchIntervalMs;
	private Integer maxRetries;
}
