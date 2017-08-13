package com.gagnepain.cashcash.domain.exception;

/**
 * Business exception
 */
public class BusinessException extends Exception {
	private final String errorKey;

	public BusinessException(final String errorKey, final String message) {
		super(message);
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}
}
