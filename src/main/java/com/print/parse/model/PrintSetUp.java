package com.print.parse.model;

/**
 * description: 打印机初始化对象
 * 
 * @author don
 * @date 2016年6月16日 下午4:36:47
 *
 */
public class PrintSetUp {
	private int width;
	private int height;
	/**
	 * 票纸类型 0:间隙纸，1：黑标纸
	 */
	private int type;
	/**
	 * 浓度0-15
	 */
	private int density;
	/**
	 * 间隙宽度 mm
	 */
	private int gapWidth;
	/**
	 * 纵轴偏移量mm
	 */
	private int printOffset;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDensity() {
		return density;
	}

	public void setDensity(int density) {
		this.density = density;
	}

	public int getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(int gapWidth) {
		this.gapWidth = gapWidth;
	}

	public int getPrintOffset() {
		return printOffset;
	}

	public void setPrintOffset(int printOffset) {
		this.printOffset = printOffset;
	}

}
