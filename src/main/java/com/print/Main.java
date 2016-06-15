package com.print;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.communication.TicketTemplate;
import com.communication.TicketsData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class Main {
	public interface TscLibDll extends Library {
		TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary("./src/main/resouces/imgs/TSCLIB", TscLibDll.class);

		int about();

		int openport(String pirnterName);

		int closeport();

		int sendcommand(String printerCommand);

		// densityé¹å«æ¸å®å¨Ã¹é¾å²î0-15é¿æ¶îensorç¼î¥î§é©åæéï¿½0å¨ææ·é¼å²å¨ç»±ï¿½1å§æ¶å¶éï½æå¨årticalé¿æ¶å²¸ç»®ï¹å¼½é¥Ñï¿½î¢æé¾î½ï¼é çç¹ç»±ï¿½
		int setup(String width, String height, String speed, String density, String sensor, String vertical, String offset);

		int downloadpcx(String filename, String image_name);

		int barcode(String x, String y, String type, String height, String readable, String rotation, String narrow, String wide, String code);

		int printerfont(String x, String y, String fonttype, String rotation, String xmul, String ymul, String text);

		int clearbuffer();

		int printlabel(String set, String copy);

		int formfeed();

		int nobackfeed();

		// éæ¶ãç¼å¬«îå¦¯åî rotationé¿æ¶ç¢å¦«åæ½ªé¡å¾æ½¡é¼è¾¾è®£ç»±æ¿æé¡æ¨»î§éæ¤æ· fontstyleé¿æ¶³æ·0éºå¶æ´¤é£îæéï¿½1éºåç²ç¼å¬®æéï¿½2ç¼î½ãç¼å¬®æéï¿½3ç¼î½îéï¿½
		// fontunderlineé¿æ¶³æ·0å¨æ³îé¨æ¿çªéï¼ç¤1é¸æ°¾æ· szFaceNameéæ¶ãç¼å¬®å´¥éï¿½
		int windowsfont(int x, int y, int fontheight, int rotation, int fontstyle, int fontunderline, String szFaceName, String content);
	}

	public static void main(String[] args) throws Exception {
		
		
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
		// printY();
		// printX();æ¤ï¸¼æ· BACKFEED
		// TscLibDll.INSTANCE.sendcommand("PUTPCX 550,10,\"UL.PCX\"");
		 StringBuilder s = new StringBuilder();
		 s.append("CLS" + "\n");
		// QRCODE X, Y, ECC Level, cell width, mode, rotation, [model,
		// mask,]"Data stringé³ã¯æ·
		 s.append("QRCODE 560,750,H,5,A,180,M2,S7,\"马老二是个二货\"");
		// s.append("PDF417 600,1300,400,200,180,\"Without Options\"");
		// s.append("QRCODE 400,950,H,4,A,0,\"ABCabc123\""+"\n");
		// s.append("QRCODE 160,160,H,4,A,0,\"123ABCabc\""+"\n");
		// s.append("QRCODE 310,310,H,4,A,0,\"é¸æ¥åª½éå¨îéæ»²Cabc123\""+"\n");
		// s.append("PRINT 1,1"+"\n");
		 TscLibDll.INSTANCE.sendcommand(s.toString());
		 TscLibDll.INSTANCE.printlabel("1", "1");
		// TscLibDll.INSTANCE.sendcommand("HOME");
//		printTest();
		// printAreaPoint();
		// printData();
		close();

	}

	/**
	 * description:
	 *
	 *
	 * @author don
	 * @throws Exception
	 * @date 2016æ¥ çæ·6éºå ¬æ·14éºå¿æ· å¨æ³ï¹¤å®ï¿½10:30:42
	 */
	private static void printData() throws Exception {
		List<TicketTemplate> list = TicketsData.getTicketsData("1420c088-bd64-416b-bf81-1eb3e103b470");
		for (TicketTemplate ticketTemplate : list) {
			TscLibDll.INSTANCE.clearbuffer();

			String s = ticketTemplate.getProperties();

			String[] array = s.split(";");
			if (array == null || array.length <= 1) {
				return;
			}
			Gson gson = new Gson();
			for (int i = 2; i < array.length; i++) {
				Map<String, Object> map = gson.fromJson(array[i], new TypeToken<HashMap<String, Object>>() {
				}.getType());

				TscLibDll.INSTANCE.windowsfont((int) (double) map.get("x"), (int) (double) map.get("y"), 48, 90, 1, 1, "arial", map.get("text").toString());
				// System.out.println((String) map.get("text"));
				// System.out.println(map.get("x"));
				// System.out.println(map.get("y"));
			}
			TscLibDll.INSTANCE.printlabel("1", "1");

		}

	}

	public static void setUp() {
		int result = TscLibDll.INSTANCE.openport("TSC T-300A");
		result = TscLibDll.INSTANCE.setup("80", "194", "6", "6", "1", "6", "0");
	}

	public static void printTest() throws UnsupportedEncodingException {
		System.setProperty("jna.encoding","GBK");
		TscLibDll.INSTANCE.clearbuffer();

		// 1/12
		String content = getTime();
		int x = 510;
		int y = 540;

		String xmString = "";
		String xmlUTF8 = "";
		
		xmString = new String("中文".getBytes(), "gbk");
		
		System.out.println(xmString);
		
		int result = TscLibDll.INSTANCE.windowsfont(530, 1150, 48, 180, 1, 1, "宋体", "中文");
		
		String ss = new String(xmString.getBytes("utf-8"), "gbk");
		
		System.out.println(ss);
		
		// TscLibDll.INSTANCE.downloadpcx("./src/main/resouces/imgs/icon.jpg",
		// "icon.jpg");
		// TscLibDll.INSTANCE.sendcommand("TEXT 510,1540,\"SIMSUNB.TTF\",180,1,1,\"\\[\"]å¨æå½éï¿½ Type Font Test Print\\[\"]\"");

		// TscLibDll.INSTANCE.printerfont("510", "1540", "TST24.BF2", "180",
		// "1", "1", "å¨æå½éï¿½ DLL Test!!");
		// System.out.println("print result :" + result);
		TscLibDll.INSTANCE.printlabel("1", "1");
	}

	public static void printY() {
		TscLibDll.INSTANCE.clearbuffer();

		int step = 12;
		// yéçæ·
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
		// xéçæ·
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
				// xéçæ·
				TscLibDll.INSTANCE.windowsfont(x, 0, step, 90, 1, 1, "arial", x + "");
			}
			if (x % (step / 2) == 0) {
				// å¨æå¾é¤ï¿½
				if (x == 0) {
					continue;
				}
				TscLibDll.INSTANCE.windowsfont(x, x * (20 / 8), step, 180, 1, 1, "arial", x + "");
			}
		}

		// yéçæ·
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
