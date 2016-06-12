package com.print;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class Main {
	public interface TscLibDll extends Library {
		TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary("TSCLIB", TscLibDll.class);

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

	/*public static void main(String[] args) {
		// TscLibDll.INSTANCE.about();
		// TscLibDll.INSTANCE.openport("TSC T-300A");
		// TscLibDll.INSTANCE.downloadpcx("C:\\UL.PCX", "UL.PCX");
		// TscLibDll.INSTANCE.sendcommand("REM ***** This is a test by JAVA. *****");

		// TscLibDll.INSTANCE.setup("200", "80", "2", "2", "1", "29", "0");
		// TscLibDll.INSTANCE.clearbuffer();

		// TscLibDll.INSTANCE.sendcommand("PUTPCX 550,10,\"UL.PCX\"");
		// TscLibDll.INSTANCE.printerfont ("100", "10", "3", "0", "1", "1",
		// "(JAVA) DLL Test!!");
		// TscLibDll.INSTANCE.barcode("100", "40", "128", "50", "1", "0", "2",
		// "2", "123456789");

		// TscLibDll.INSTANCE.windowsfont(100, 100, 48, 90, 3, 1, "arial",
		// "DEG 34340");
		// TscLibDll.INSTANCE.windowsfont(400, 200, 48, 90, 3, 1, "arial",
		// "DEG 90");
		// TscLibDll.INSTANCE.windowsfont(400, 200, 48, 180, 3, 1, "arial",
		// "DEG 180");
		// TscLibDll.INSTANCE.windowsfont(400, 200, 48, 270, 3, 1, "arial",
		// "DEG 270");

		// TscLibDll.INSTANCE.printlabel("1", "1");

		setUp();
		// clear();
		// jump();
		printY();
//		printX();
		// printTest();
		// printAreaPoint();
		close();

	}*/

	public static void setUp() {
		int result = TscLibDll.INSTANCE.openport("TSC T-300A");
		result = TscLibDll.INSTANCE.setup("80", "194", "6", "6", "1", "6", "0");
	}

	public static void printTest() {
		TscLibDll.INSTANCE.clearbuffer();

		// 1/12
		String content = getTime();
		int x = 900;
		int y = 0;

		int result = TscLibDll.INSTANCE.windowsfont(x, y, 48, 90, 1, 1, "arial", "..");
		System.out.println("print result :" + result);
		System.out.println(content + "\t x:" + x + "\t y:" + y);
		TscLibDll.INSTANCE.printlabel("1", "1");
	}

	public static void printY() {
		TscLibDll.INSTANCE.clearbuffer();

		int step = 12;
		// y轴
		for (int y = 0; y <= 194 * 12; y++) {
			if (y % step == 0) {
				System.out.println(y);
				TscLibDll.INSTANCE.windowsfont(880, y, step, 180, 1, 1, "arial", y + "");
			}
		}
		TscLibDll.INSTANCE.printlabel("1", "1");
	}
	
	public static void printX() {
		TscLibDll.INSTANCE.clearbuffer();

		int step = 12;
		// x轴
		for (int x = 0; x < 80 * 12; x++) {
			if (x % step == 0) {
				if (x == 0) {
					continue;
				}
				TscLibDll.INSTANCE.windowsfont(x, 500, step, 90, 1, 1, "arial", x + "");
			}
		}
		TscLibDll.INSTANCE.printlabel("1", "1");
	}


	public static void jump() {
		TscLibDll.INSTANCE.formfeed();
		TscLibDll.INSTANCE.clearbuffer();
	}

	public static void clear() {
		TscLibDll.INSTANCE.formfeed();
		TscLibDll.INSTANCE.clearbuffer();
	}

	public static void printAreaPoint() {
		TscLibDll.INSTANCE.clearbuffer();

		int step = 30;
		for (int x = 0; x < 80 * 12; x++) {
			if (x % step == 0) {
				// x轴
				TscLibDll.INSTANCE.windowsfont(x, 0, step, 90, 1, 1, "arial", x + "");
			}
			if (x % (step / 2) == 0) {
				// 中线
				if (x == 0) {
					continue;
				}
				TscLibDll.INSTANCE.windowsfont(x, x * (20 / 8), step, 180, 1, 1, "arial", x + "");
			}
		}

		// y轴
		for (int y = 0; y < 194 * 12; y++) {
			if (y % step == 0) {
				if (y == 0) {
					continue;
				}
				TscLibDll.INSTANCE.windowsfont(0, y, step, 180, 1, 1, "arial", y + "");
			}
		}
		TscLibDll.INSTANCE.printlabel("1", "1");
	}

	public static void close() {
		TscLibDll.INSTANCE.closeport();
	}

	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date());
	}
}
