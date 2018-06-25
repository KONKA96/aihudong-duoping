package com.model;

import java.io.Serializable;

public class Err implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errcode;
	
	private String msg;
	
	public Err() {
		super();
	}

	public Err(int errcode, String msg) {
		super();
		this.errcode = errcode;
		this.msg = msg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Err [errcode=" + errcode + ", msg=" + msg + "]";
	}
	
	
}
