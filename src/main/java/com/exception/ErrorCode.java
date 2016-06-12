package com.exception;

/**
 * description:
 * @author don
 * @date 2015年7月22日 下午5:53:07
 *
 */
public enum ErrorCode {
	
	ERROR_ILLEGAL_PARAMETER(10001, "传入参数不合法"), 
	ERROR_ILLEGAL_SQL_INSERT(10002, "数据库插入异常"), 
	ERROR_ILLEGAL_SQL_UPDATE(10003, "数据库更新异常"), 
	
	

	ERROR_NULL_FRAME(10000, "主窗体未创建"), 
	ERROR_NULL_MENUBAR(10001, "菜单栏未创建"), 
	
	;

	private int ErrorCode;// 返回码
	private String ErrorMsg;// 错误信息

	private ErrorCode(Integer code, String msg) {
		this.ErrorCode = code;
		this.ErrorMsg = msg;
	}

	public int Code() {
		return ErrorCode;
	}

	public String Msg() {
		return ErrorMsg;
	}

}
