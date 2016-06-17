package com.exception;


/**
 * description: 
 * 
 * @author don
 * @date 2016年6月8日 下午4:15:32
 *
 */
public class PrintException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;
	private String reason;

	private PrintException(String message) {
		super(message);
	}

	public static PrintException Error(int code, String reason) {
		PrintException ex = new PrintException(reason);
		ex.setCode(code);
		ex.setReason(reason);
		return ex;
	}

	public static PrintException Error(ErrorCode errorCode) {
		return PrintException.Error(errorCode.Code(), errorCode.Msg());
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
