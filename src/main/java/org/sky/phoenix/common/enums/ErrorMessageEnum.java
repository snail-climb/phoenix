package org.sky.phoenix.common.enums;

/**
 * <p>
 *
 * </p>
 *
 * @author Forrest
 * @since 2024-04-10 15:03
 */
public enum ErrorMessageEnum {

	PARAMETER_REQUIRED("Parameter is required!");

	private final String message;

	ErrorMessageEnum(String message) {
		this.message = message;
	}

	public static String getErrorMessage(ErrorMessageEnum errorMessageEnum) {
		return errorMessageEnum.message;
	}
}
