package com.strongit.iss.exception;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1529224080023368068L;

	public BaseException() {
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable t) {
		super(t);
	}

	public BaseException(String message, Throwable t) {
		super(message, t);
	}
}