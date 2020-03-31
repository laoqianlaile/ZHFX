package com.strongit.iss.exception;

/**
 * 表示业务逻辑异常
 * 
 * @author William
 *
 */
public class BusinessServiceException extends BaseException {
	private static final long serialVersionUID = -8171245571221202343L;

	public BusinessServiceException(String message) {
		super(message);
	}

	public BusinessServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getCasuseMessage() {
		if (super.getCause() != null) {
			return super.getCause().getMessage();
		} else {
			return super.getMessage();
		}
	}
	
	@Override
	public String getMessage(){
		if (super.getCause() != null) {
			return String.format("%s-%s",super.getMessage(),super.getCause().getMessage());
		} else {
			return super.getMessage();
		}
	}
}
