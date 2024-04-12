package org.sky.phoenix.common;

import lombok.extern.slf4j.Slf4j;
import org.sky.phoenix.common.enums.ErrorMessageEnum;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 14:21
 */
@Slf4j
public class SystemException extends RuntimeException {

	public SystemException() {
		super();
	}

	public SystemException(String message) {
		super(message);
	}

	private static SystemException throwSystemException(Class<?> clazz, String errorMessage) {
		String className = clazz.getName();
		String message = String.format("【%s ERROR】%s", className, errorMessage);
		log.error(message);
		return new SystemException(message);
	}

	public static SystemException throwSystemException(Class<?> clazz, Exception e) {
		return throwSystemException(clazz, e.getMessage());
	}

	public static SystemException throwSystemException(Class<?> clazz, ErrorMessageEnum errorMessageEnum) {
		return throwSystemException(clazz, ErrorMessageEnum.getErrorMessage(errorMessageEnum));
	}
}
