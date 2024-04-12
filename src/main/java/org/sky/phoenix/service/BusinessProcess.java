package org.sky.phoenix.service;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.sky.phoenix.domain.bo.DatabaseSchemaInfo;
import org.sky.phoenix.domain.bo.FieldLineage;
import org.sky.phoenix.domain.bo.LineageInfo;
import org.sky.phoenix.domain.bo.RawLineage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.sky.phoenix.common.utils.BusinessUtil.buildFieldLineage;
import static org.sky.phoenix.common.utils.SystemUtil.createObjectMapper;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-11 15:55
 */
public class BusinessProcess {

	public static ProcessFunction<String, LineageInfo> jsonParseProcess(OutputTag<String> errorOutputTag) {
		ObjectMapper objectMapper = createObjectMapper();
		return new ProcessFunction<>() {
			@Override
			public void processElement(String json, Context ctx, Collector<LineageInfo> collector) {
				try {
					JsonNode jsonNode = objectMapper.readTree(json);
					String message = jsonNode.get("message").asText();
					LineageInfo lineageInfo = objectMapper.readValue(message, LineageInfo.class);
					collector.collect(lineageInfo);
				} catch (Exception e) {
					ctx.output(errorOutputTag, json);
				}
			}
		};
	}

	public static ProcessFunction<LineageInfo, RawLineage> rawLineageProcess() {
		return new ProcessFunction<>() {
			@Override
			public void processElement(LineageInfo lineageInfo, Context ctx, Collector<RawLineage> collector) {
				LocalDateTime queryStartTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(lineageInfo.getTimestamp()), ZoneId.of("Asia/Shanghai"));
				RawLineage rawLineage = RawLineage.builder()
					.hqlKey(lineageInfo.getHash())
					.rawLineage(lineageInfo.getQueryText())
					.execStartTime(queryStartTime)
					.execSpendMs(lineageInfo.getDuration())
					.createTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
					.build();
				collector.collect(rawLineage);
			}
		};
	}

	public static ProcessFunction<LineageInfo, List<FieldLineage>> fieldLineageProcess() {
		return new ProcessFunction<>() {
			@Override
			public void processElement(LineageInfo lineageInfo, Context ctx, Collector<List<FieldLineage>> collector) {
				List<FieldLineage> fieldLineageList = new ArrayList<>();
				List<LineageInfo.Edge> edges = lineageInfo.getEdges();
				List<LineageInfo.Vertices> vertices = lineageInfo.getVertices();
				edges = edges.stream().filter(edge -> Objects.equals(edge.getEdgeType(), "PROJECTION")).collect(Collectors.toList());
				edges.forEach(edge -> {
					List<DatabaseSchemaInfo> targetData = buildFieldLineage(vertices, edge.getTargets());
					List<DatabaseSchemaInfo> sourceData = buildFieldLineage(vertices, edge.getSources());
					for (DatabaseSchemaInfo targetInfo : targetData) {
						for (DatabaseSchemaInfo sourceInfo : sourceData) {
							FieldLineage fieldLineage = FieldLineage.builder()
								.targetHqlKey(lineageInfo.getHash())
								.targetDb(targetInfo.getDb())
								.targetTable(targetInfo.getTable())
								.targetField(targetInfo.getField())
								.downstreamDb(sourceInfo.getDb())
								.downstreamTable(sourceInfo.getTable())
								.downstreamFiled(sourceInfo.getField())
								.createTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
								.build();
							fieldLineage.setHashcode(Objects.hashCode(fieldLineage.toString()));
							fieldLineageList.add(fieldLineage);
						}
					}
				});
				collector.collect(fieldLineageList);
			}
		};
	}
}
