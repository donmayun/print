package com.communication;

import java.util.List;

import com.communication.utils.HttpRequest;
import com.communication.utils.RestResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * description: 查询打印数据
 * 
 * @author don
 * @date 2016年6月12日 上午10:14:23
 *
 */
public class TicketsData {
	
	public static List<TicketTemplate> getTicketsData(String cacheKey) throws Exception {
		String result = HttpRequest.sendGet("http://localhost:8080/console/admin/ticket/print/data.do", "cacheKey=" + cacheKey);
		System.out.println(result);
		Gson gson = new Gson();
		RestResult rs = gson.fromJson(result, RestResult.class);

		if (rs.getCode() != 200) {
			throw new Exception(rs.getData().get("error").toString());
		} else {
			List<TicketTemplate> list = gson.fromJson(gson.toJson(rs.getData().get("list")), new TypeToken<List<TicketTemplate>>() {
			}.getType());
			return list;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getTicketsData("42f2a2ba-0458-439f-8536-bd7e136cb3f7"));
	}
}
