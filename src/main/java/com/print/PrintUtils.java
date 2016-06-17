package com.print;

import java.util.List;

import com.print.parse.model.PrintQrcode;
import com.print.parse.model.PrintSetUp;
import com.print.parse.model.PrintText;

/**
 * description: 打印二次封装工具类
 * 
 * @author don
 * @date 2016年6月15日 下午5:47:39
 *
 */
public class PrintUtils {

	public static void open() {
		TscLibDll.INSTANCE.openport("TSC T-300A");
	}

	public static void setUp(PrintSetUp printSetUp) {
		System.setProperty("jna.encoding", "GBK");
		TscLibDll.INSTANCE.setup(printSetUp.getHeight() + "", (printSetUp.getWidth() - printSetUp.getGapWidth()) + "", "6", printSetUp.getDensity() + "6",
				printSetUp.getType() + "", printSetUp.getGapWidth() + "", printSetUp.getPrintOffset() + "");
	}

	public static void close() {
		TscLibDll.INSTANCE.closeport();
	}

	public static int getStatus() {
		return TscLibDll.INSTANCE.usbportqueryprinter();
	}

	public static String StatusDes(int type) {
		switch (type) {
		case 0:
			return "[" + type + "]" + "待机中";
		case 1:
			return "[" + type + "]" + "打印头未关";
		case 2:
			return "[" + type + "]" + "卡纸";
		case 3:
			return "[" + type + "]" + "卡纸并且打印头未关";
		case 4:
			return "[" + type + "]" + "缺纸";
		case 5:
			return "[" + type + "]" + "缺纸并且打印头未关";
		case 6:
			return "[" + type + "]" + "外盖开启(选购)";
		case 7:
			return "[" + type + "]" + "环境温度超过范围 (选购)";
		case 8:
			return "[" + type + "]" + "缺碳带";
		case 9:
			return "[" + type + "]" + "缺碳带并且打印头未关";
		case 10:
			return "[" + type + "]" + "缺碳带并且卡纸";
		case 11:
			return "[" + type + "]" + "缺碳带并卡纸及开启打印头";
		case 12:
			return "[" + type + "]" + "缺碳带并且缺纸";
		case 13:
			return "[" + type + "]" + "缺碳带、缺纸、开启打印头";
		case 16:
			return "[" + type + "]" + "暂停";
		case 32:
			return "[" + type + "]" + "打印中";
		default:
			return "[" + type + "]" + "状态不可识别";
		}
	}

	/**
	 * description: windowsfont
	 *
	 * @param text
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:52:37
	 */
	public static int windowsText(PrintText text) {
		System.out.println(text);
		return TscLibDll.INSTANCE.windowsfont(text.getX(), text.getY(), text.getFontHeight(), text.getRotation().intValue(), text.getFontstyle(), 0,
				text.getFontName(), text.getText());
	}

	/**
	 * description: 多个二维码打印
	 *
	 * @param qrList
	 *
	 * @author don
	 * @date 2016年6月17日 下午3:29:58
	 */
	public static void printQrcodes(List<PrintQrcode> qrList) {
		for (PrintQrcode printQrcode : qrList) {
			printQrcode(printQrcode);
		}
	}

	/**
	 * description: 单个二维码打印
	 *
	 * @param printQrcode
	 *
	 * @author don
	 * @date 2016年6月17日 下午3:34:20
	 */
	public static void printQrcode(PrintQrcode printQrcode) {
		System.out.println(printQrcode.getPrintCode());
		TscLibDll.INSTANCE.sendcommand(printQrcode.getPrintCode());
	}
}
