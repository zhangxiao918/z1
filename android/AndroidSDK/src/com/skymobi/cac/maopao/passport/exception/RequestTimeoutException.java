package com.skymobi.cac.maopao.passport.exception;

public class RequestTimeoutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6475749597701957612L;

	public RequestTimeoutException() {
		super();
	}

	public RequestTimeoutException(String message) {
		super(message);
	}

	public RequestTimeoutException(Throwable cause) {
		super(cause);
	}

	public RequestTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}