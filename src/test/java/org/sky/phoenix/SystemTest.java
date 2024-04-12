package org.sky.phoenix;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sky.phoenix.domain.bo.LineageInfo;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 15:49
 */
public class SystemTest {

	@Test
	public void jsonParseTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String message = new String(Files.readAllBytes(Paths.get("input/HiveLineageJsonDemo.txt")));
		LineageInfo lineageInfo = objectMapper.readValue(message, LineageInfo.class);
	}
}
