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
 * @since 2024-04-10 12:23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RawLineage {

	private String hqlKey;
	private String rawLineage;
	private LocalDateTime execStartTime;
	private Long execSpendMs;
	private LocalDateTime createTime;
}
