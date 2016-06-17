package com.print;

import java.util.List;

import com.communication.TicketTemplate;
import com.print.parse.Parse;
import com.print.parse.model.PrintQrcode;
import com.print.parse.model.PrintText;

/**
 * description:
 * 
 * @author don
 * @date 2016年6月15日 下午5:40:48
 *
 */
public class PrintService {

	/**
	 * description:
	 *
	 * @param list
	 *
	 * @author don
	 * @date 2016年6月15日 下午5:41:25
	 */
	public void printTicket(List<TicketTemplate> list) {
		PrintUtils.open();

		for (TicketTemplate ticketTemplate : list) {
			TscLibDll.INSTANCE.clearbuffer();
			// 票纸初始化
			PrintUtils.setUp(ticketTemplate.getPrintSetUp());

			String s = ticketTemplate.getProperties();
			String[] array = s.split(";");
			if (array == null || array.length <= 1) {
				return;
			}

			// 二维码解析
			List<PrintQrcode> qrList = Parse.createQrcodeParse(array[1]).allParseAndOut();
			PrintUtils.printQrcodes(qrList);
			// 文本解析
			PrintText text = null;
			for (int i = 2; i < array.length; i++) {
				text = Parse.createTextParse(array[i]).allParseAndOut();
				PrintUtils.windowsText(text);
			}
			TscLibDll.INSTANCE.printlabel("1", "1");
		}
		PrintUtils.close();
	}

	/**
	 * description: 获取打印机状态
	 *
	 *
	 * @author don
	 * @date 2016年6月16日 下午2:09:58
	 */
	public int getStatus() {
		PrintUtils.open();
		int status = TscLibDll.INSTANCE.usbportqueryprinter();
		PrintUtils.close();
		return status;
	}

}
