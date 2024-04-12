package org.sky.phoenix.domain.bo;

import lombok.Data;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 15:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LineageInfo {

	private String version;
	private String user;
	private Long timestamp;
	private Long duration;
	private List<String> jobIds;
	private String engine;
	private String database;
	private String hash;
	private String queryText;
	private List<Edge> edges;
	private List<Vertices> vertices;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	public static class Edge {
		private List<Integer> sources;
		private List<Integer> targets;
		private String expression;
		private String edgeType;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	public static class Vertices {
		private Integer id;
		private String vertexType;
		private String vertexId;
	}
}
