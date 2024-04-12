package org.sky.phoenix.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-11 14:20
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabaseSchemaInfo {

	private String db;
	private String table;
	private String field;
}
