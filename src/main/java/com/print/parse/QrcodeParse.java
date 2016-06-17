package com.print.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.print.parse.model.PrintQrcode;

/**
 * description:
 * 
 * @author don
 * @date 2016年6月17日 下午3:10:44
 *
 */
public class QrcodeParse {
	private List<PrintQrcode> qrcodeList;
	private List<Map<String, Object>> mapList;
	private int total;

	/**
	 * @param originS
	 */
	public QrcodeParse(String originS) {
		total = 0;
		Gson gson = new Gson();
		String[] array = originS.split("#");
		qrcodeList = new ArrayList<>();
		mapList = new ArrayList<>();
		for (String string : array) {
			Map<String, Object> originMap = gson.fromJson(string, new TypeToken<HashMap<String, Object>>() {
			}.getType());
			if (originMap == null || originMap.isEmpty()) {
				continue;
			}
			mapList.add(originMap);
			qrcodeList.add(new PrintQrcode());
			total++;
		}

	}

	/**
	 * description: 坐标解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:16:08
	 */
	public QrcodeParse coorParse() {
		for (int i = 0; i < total; i++) {
			int x = (int) (double) mapList.get(i).get("x");
			int y = (int) (double) mapList.get(i).get("y");
			qrcodeList.get(i).setX(x);
			qrcodeList.get(i).setY(y);
		}
		return this;
	}

	/**
	 * description: 文本解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:50:31
	 */
	public QrcodeParse contentParse() {
		for (int i = 0; i < total; i++) {
			qrcodeList.get(i).setContext(mapList.get(i).get("qrcontent").toString());
		}
		return this;
	}

	/**
	 * description:
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月17日 下午3:18:27
	 */
	public QrcodeParse sizeParse() {
		for (int i = 0; i < total; i++) {
			int size = (int) (double) mapList.get(i).get("qrsize");
			qrcodeList.get(i).setSize(size);
		}
		return this;
	}

	/**
	 * description: 输出
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:49:33
	 */
	public List<PrintQrcode> output() {
		return qrcodeList;
	}

	/**
	 * description: 全解析并输出
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:56:04
	 */
	public List<PrintQrcode> allParseAndOut() {
		return this.coorParse().sizeParse().contentParse().output();
	}
}
