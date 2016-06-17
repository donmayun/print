package com.print.parse;


/**
 * description:
 * 
 * @author don
 * @date 2016年6月16日 下午4:58:58
 *
 */
public class Parse {

	/**
	 * description:
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午4:59:13
	 */
	public static TextParse createTextParse(String originS) {
		return new TextParse(originS);
	}

	/**
	 * description: 二维码解析
	 *
	 * @param string
	 *
	 * @author don
	 * @date 2016年6月17日 下午3:10:03
	 */
	public static QrcodeParse createQrcodeParse(String originS) {
		return new QrcodeParse(originS);
	};

}
