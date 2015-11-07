package com.spike.springdata.jpa.exception;

public class RuntimeBussinessException extends RuntimeException {
	private static final long serialVersionUID = 9043782295716360818L;

	public RuntimeBussinessException(String message) {
		super(message);
	}

	public RuntimeBussinessException(String message, Throwable cause) {
		super(message, cause);
	}
}