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
	
	

	ERROR_NULL_FRAME(10004, "主窗体未创建"), 
	ERROR_NULL_MENUBAR(10005, "菜单栏未创建"), 
	
	
	ERROR_NULL_TEXT_S(11001, "解析的文本对象字符串不能为空"), 
	ERROR_NULL_TEXT_JSON(11002, "解析的文本对象JSON转换为空"), 
	
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
