package com.print.parse;

import java.util.HashMap;
import java.util.Map;

import com.exception.ErrorCode;
import com.exception.PrintException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.print.parse.model.PrintText;

/**
 * description: 文本解析接口
 * 
 * @author don
 * @date 2016年6月16日 下午5:00:21
 *
 */
public class TextParse {
	private PrintText textItem;
	private Map<String, Object> originMap;

	public TextParse(String originS) {
		Gson gson = new Gson();
		originMap = gson.fromJson(originS, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		if (originMap == null || originMap.isEmpty()) {
			throw PrintException.Error(ErrorCode.ERROR_NULL_TEXT_S);
		}
		textItem = new PrintText();
	}

	/**
	 * description: 坐标解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:16:08
	 */
	public TextParse coorParse() {
		int x = (int) (double) originMap.get("x");
		int y = (int) (double) originMap.get("y");
		textItem.setX(x);
		textItem.setY(y);
		return this;
	}

	/**
	 * description: 粗斜体解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:35:26
	 */
	public TextParse fontStyleParse() {
		byte[] bytes = new byte[2];

		String italic = (String) originMap.get("font-style");
		if (italic != null && italic.trim().equals("italic")) {
			bytes[0] = 1;
		}

		String weight = (String) originMap.get("font-weight");
		if (weight != null && weight.trim().equals("bold")) {
			bytes[1] = 1;
		}
		int fontstyle = bytes[0] + (bytes[1] << 1);

		textItem.setFontstyle(fontstyle);
		return this;
	}

	/**
	 * description: 字体解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:38:58
	 */
	public TextParse fontNameParse() {
		String fontName = (String) originMap.get("font-family");
		if (fontName != null) {
			textItem.setFontName(fontName);
		}

		return this;
	}

	/**
	 * description: 字体大小解析
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:41:34
	 */
	public TextParse fontSizeParse() {
		Double fontSize = (Double) originMap.get("print-font-size");
		int fontHeight = 40;
		if (fontSize != null) {
			fontHeight = (int) fontSize.doubleValue();
		}
		textItem.setFontHeight(fontHeight);
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
	public TextParse textParse() {
		textItem.setText(originMap.get("text").toString());
		return this;
	}

	public TextParse transformParse() {
		String transform = (String) originMap.get("transform");
		if (transform != null) {
			textItem.setRotation(90 - Integer.parseInt(transform.replace("rotate(", "").replace("deg)", "")));
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
	public PrintText output() {
		return textItem;
	}

	/**
	 * description: 全解析并输出
	 *
	 * @return
	 *
	 * @author don
	 * @date 2016年6月16日 下午5:56:04
	 */
	public PrintText allParseAndOut() {
		return this.fontStyleParse().transformParse().coorParse().fontNameParse().fontSizeParse().textParse().output();
	}
}
