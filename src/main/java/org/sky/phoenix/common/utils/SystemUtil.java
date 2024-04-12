package org.sky.phoenix.common.utils;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 14:08
 */
public class SystemUtil {

	public static ObjectMapper createObjectMapper() {
		return new ObjectMapper();
	}

	public static boolean isContainsTwoDots(String str) {
		return str.matches(".*\\..*\\..*");
	}
}
