package com.print;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * description:
 * 
 * @author don
 * @date 2016年6月15日 下午5:44:13
 *
 */
public interface TscLibDll extends Library {
	public static TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary("./src/main/resouces/imgs/TSCLIB", TscLibDll.class);

	int about();

	int openport(String pirnterName);

	int closeport();

	int sendcommand(String printerCommand);

	// density打印浓度0-15，sensor类别，0一般，1黑标；vertical：黑标垂直间距，
	int setup(String width, String height, String speed, String density, String sensor, String vertical, String offset);

	int downloadpcx(String filename, String image_name);

	int barcode(String x, String y, String type, String height, String readable, String rotation, String narrow, String wide, String code);

	int printerfont(String x, String y, String fonttype, String rotation, String xmul, String ymul, String text);

	int clearbuffer();

	int printlabel(String set, String copy);

	int formfeed();

	int nobackfeed();

	// 字体高度 rotation：旋转角度，逆时针 fontstyle：0标准，1斜体，2粗体，3粗斜
	// fontunderline：0下划线，1否 szFaceName字体名
	int windowsfont(int x, int y, int fontheight, int rotation, int fontstyle, int fontunderline, String szFaceName, String content);

}
