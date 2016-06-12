package com.exception;


/**
 * description: 
 * 
 * @author don
 * @date 2016年6月8日 下午4:15:32
 *
 */
public class FrameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;
	private String reason;

	private FrameException(String message) {
		super(message);
	}

	public static FrameException Error(int code, String reason) {
		FrameException ex = new FrameException(reason);
		ex.setCode(code);
		ex.setReason(reason);
		return ex;
	}

	public static FrameException Error(ErrorCode errorCode) {
		return FrameException.Error(errorCode.Code(), errorCode.Msg());
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
