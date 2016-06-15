package com.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.communication.TicketTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.print.Main.TscLibDll;

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
		PrintUtils.setUp();
		System.setProperty("jna.encoding", "GBK");

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

				TscLibDll.INSTANCE.windowsfont((int) (double) map.get("x"), (int) (double) map.get("y"), 48, 90, 0, 0, "宋体", map.get("text").toString());
			}
			TscLibDll.INSTANCE.printlabel("1", "1");
		}
		PrintUtils.close();
	}

}
