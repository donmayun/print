package com.print.parse.model;

import org.apache.commons.lang.StringUtils;

/**
 * description: 文本打印对象
 * 
 * @author don
 * @date 2016年6月16日 下午4:42:11
 *
 */
public class PrintText {
	private int x;
	private int y;
	private int fontHeight;
	/**
	 * rotation：旋转角度，逆时针
	 */
	private Integer rotation;
	/**
	 * fontstyle：0标准，1斜体，2粗体，3粗斜
	 */
	private int fontstyle;
	/**
	 * fontunderline：下划线，0否,1是
	 */
	private int fontunderline;
	/**
	 * 字体名
	 */
	private String fontName;
	private String text;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public Integer getRotation() {
		if (rotation == null) {
			return 90;
		}
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	public int getFontstyle() {
		return fontstyle;
	}

	public void setFontstyle(int fontstyle) {
		this.fontstyle = fontstyle;
	}

	public int getFontunderline() {
		return fontunderline;
	}

	public void setFontunderline(int fontunderline) {
		this.fontunderline = fontunderline;
	}

	public String getFontName() {
		if (StringUtils.isBlank(fontName)) {
			return "宋体";
		}
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * description:
	 */
	@Override
	public String toString() {
		return "PrintText [x=" + x + ", y=" + y + ", fontHeight=" + fontHeight + ", rotation=" + rotation + ", fontstyle=" + fontstyle + ", fontunderline="
				+ fontunderline + ", fontName=" + fontName + ", text=" + text + "]";
	}

}
