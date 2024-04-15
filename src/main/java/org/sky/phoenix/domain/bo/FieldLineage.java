package org.sky.phoenix.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 12:24
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldLineage {

	private Integer hashcode;
	private String targetHqlKey;
	private String targetDb;
	private String targetTable;
	private String targetField;
	private String downstreamDb;
	private String downstreamTable;
	private String downstreamFiled;
	private LocalDateTime createTime;
}
