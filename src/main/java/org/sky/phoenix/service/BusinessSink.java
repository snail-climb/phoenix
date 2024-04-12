package org.sky.phoenix.service;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.sky.phoenix.domain.bo.FieldLineage;
import org.sky.phoenix.domain.bo.RawLineage;
import org.sky.phoenix.domain.config.JdbcConfig;

import java.sql.Timestamp;
import java.util.List;

import static org.sky.phoenix.operator.sink.SystemJdbcSink.createJdbcSink;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-11 15:55
 */
public class BusinessSink {

	public static SinkFunction<RawLineage> rawLineageSink(JdbcConfig config) {
		return createJdbcSink(
			config,
			"INSERT INTO hive_lineage.raw_lineage(hql_key, raw_lineage, exec_start_time, exec_spend_ms, create_time)\n" +
				"VALUES (?, ?, ?, ?, ?);",
			(preparedStatement, rawLineage) -> {
				preparedStatement.setString(1, rawLineage.getHqlKey());
				preparedStatement.setString(2, rawLineage.getRawLineage());
				preparedStatement.setTimestamp(3, Timestamp.valueOf(rawLineage.getExecStartTime()));
				preparedStatement.setLong(4, rawLineage.getExecSpendMs());
				preparedStatement.setTimestamp(5, Timestamp.valueOf(rawLineage.getCreateTime()));
			}
		);
	}

	public static SinkFunction<List<FieldLineage>> fieldLineageSink(JdbcConfig config) {
		return createJdbcSink(
			config,
			"INSERT IGNORE INTO hive_lineage.field_lineage(hashcode, target_hql_key, target_db, target_table, target_field,\n" +
				"                                              downstream_db, downstream_table, downstream_filed, create_time)\n" +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
			(preparedStatement, fieldLineages) -> {
				for (FieldLineage fieldLineage : fieldLineages) {
					preparedStatement.setInt(1, fieldLineage.getHashcode());
					preparedStatement.setString(2, fieldLineage.getTargetHqlKey());
					preparedStatement.setString(3, fieldLineage.getTargetDb());
					preparedStatement.setString(4, fieldLineage.getTargetTable());
					preparedStatement.setString(5, fieldLineage.getTargetField());
					preparedStatement.setString(6, fieldLineage.getDownstreamDb());
					preparedStatement.setString(7, fieldLineage.getDownstreamTable());
					preparedStatement.setString(8, fieldLineage.getDownstreamFiled());
					preparedStatement.setTimestamp(9, Timestamp.valueOf(fieldLineage.getCreateTime()));
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			}
		);
	}
}
