package org.sky.phoenix.common.utils;

import org.sky.phoenix.domain.bo.DatabaseSchemaInfo;
import org.sky.phoenix.domain.bo.LineageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.sky.phoenix.common.utils.SystemUtil.isContainsTwoDots;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-11 14:34
 */
public class BusinessUtil {

	public static List<DatabaseSchemaInfo> buildFieldLineage(List<LineageInfo.Vertices> vertices, List<Integer> ids) {
		List<DatabaseSchemaInfo> fieldLineage = new ArrayList<>();
		for (Integer id : ids) {
			for (LineageInfo.Vertices vertex : vertices) {
				if (Objects.equals(vertex.getId(), id)) {
					String vertexId = vertex.getVertexId();
					if (isContainsTwoDots(vertexId)) {
						String[] baseData = vertexId.split("\\.");
						fieldLineage.add(DatabaseSchemaInfo.builder().db(baseData[0]).table(baseData[1]).field(baseData[2]).build());
					}
				}
			}
		}
		return fieldLineage;
	}
}
