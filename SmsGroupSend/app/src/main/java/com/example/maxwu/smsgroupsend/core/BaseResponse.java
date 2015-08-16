package com.example.maxwu.smsgroupsend.core;

import java.io.Serializable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse  implements Serializable{

	/**
	 * 
	 */
	@Expose
	private static final long serialVersionUID = 4988504610272811148L;
	@Expose
	public boolean success;
	@Expose
	public String errorCode;
	@Expose
	public String msg;
	
	@Override
	public String toString() {
	
		return "success = "+ success+",errorCode = "+errorCode+",msg = "+ msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}	
	
}
