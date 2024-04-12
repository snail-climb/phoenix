package org.sky.phoenix.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.org.yaml.snakeyaml.Yaml;
import org.sky.phoenix.domain.config.JdbcConfig;
import org.sky.phoenix.domain.config.KafkaConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.sky.phoenix.common.SystemException.throwSystemException;
import static org.sky.phoenix.common.utils.SystemUtil.createObjectMapper;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 11:13
 */
@Slf4j
public class SystemConfig {

	private static final Properties APPLICATION_CONFIG = new Properties();
	private static final Map<String, Map<String, Object>> SYSTEM_CONFIG;
	private static final Map<String, KafkaConfig> KAFKA_CONFIGS;
	private static final Map<String, JdbcConfig> JDBC_CONFIGS;

	static {
		try (InputStream applicationInput = SystemConfig.class.getClassLoader().getResourceAsStream("application.properties");
			 InputStream systemInput = SystemConfig.class.getClassLoader().getResourceAsStream("application-prod.yml")) {
			APPLICATION_CONFIG.load(applicationInput);
			SYSTEM_CONFIG = new Yaml().load(systemInput);
			KAFKA_CONFIGS = setServerConfig(KafkaConfig.class);
			JDBC_CONFIGS = setServerConfig(JdbcConfig.class);
		} catch (IOException e) {
			throw throwSystemException(SystemConfig.class, e);
		}
	}

	private static <T> Map<String, T> setServerConfig(Class<T> clazz) {
		Map<String, T> configs = new HashMap<>();
		ObjectMapper objectMapper = createObjectMapper();
		Map<String, Object> serverConfig = SYSTEM_CONFIG.getOrDefault(clazz.getSimpleName(), new HashMap<>());
		serverConfig.forEach((key, value) -> {
			try {
				T config = objectMapper.readValue(objectMapper.writeValueAsString(value), clazz);
				configs.put(key, config);
			} catch (JsonProcessingException e) {
				throw throwSystemException(SystemConfig.class, e);
			}
		});
		return configs;
	}

	public static final String APPLICATION_NAME = APPLICATION_CONFIG.getProperty("application.name");

	public static KafkaConfig getSpecifiedKafkaConfig(String sourceKey) {
		return KAFKA_CONFIGS.getOrDefault(sourceKey, null);
	}

	public static JdbcConfig getSpecifiedJdbcConfig(String sourceKey) {
		return JDBC_CONFIGS.getOrDefault(sourceKey, null);
	}
}
