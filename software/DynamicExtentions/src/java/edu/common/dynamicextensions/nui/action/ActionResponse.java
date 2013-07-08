package edu.common.dynamicextensions.nui.action;

import java.io.Serializable;

public class ActionResponse implements Serializable {
	private static final long serialVersionUID = 1974888338338727366L;

	private int status;
	
	private String message;
	
	private String errorMessage;

	public int getStatus() {
		return status;
	}

	public void setSuccess() {
		this.status = 0;
	}
	
	public void setFailure() {
		this.status = 1;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
