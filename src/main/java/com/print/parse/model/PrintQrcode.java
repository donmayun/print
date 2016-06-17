package com.print.parse.model;

/**
 * description: 二维码打印对象
 * 
 * @author don
 * @date 2016年6月17日 下午3:11:11
 *
 */
public class PrintQrcode {
	private int x;
	private int y;
	private int size;
	private String context;

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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getPrintCode() {
		return "QRCODE " + x + "," + y + ",H," + size + ",A,180,M2,S7,\"" + context + "\"";
	}

}
