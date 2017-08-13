package com.gagnepain.cashcash.web.rest.errors;

import java.io.Serializable;

/**
 * Generic error message.
 */
public class CashError implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String message;

	private final String errorKey;

	public CashError(final String errorKey, final String message) {
		this.errorKey = errorKey;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorKey() {
		return errorKey;
	}
}
