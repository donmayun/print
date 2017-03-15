package netty.controller;

import com.communication.TicketTemplate;
import com.communication.TicketsData;
import com.print.PrintService;
import com.print.PrintUtils;
import netty.http.context.HttpMethodRequest;
import netty.http.context.HttpMethodResponse;
import netty.http.mvc.RequestMapping;
import netty.http.mvc.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Don on 2016/1/13.
 */
@Controller
public class TestController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/test.do")
	@ResponseBody
	public Map<String, String> service(HttpMethodRequest request, HttpMethodResponse response) {
		System.out.println("1111");
		Map<String, String> map = new HashMap<>();
		map.put("111", "222");
		return map;
	}

	@RequestMapping("/test2.do")
	public void service1(HttpMethodRequest request, HttpMethodResponse response, String name) {
		System.out.println("2222");
		response.println("hello " + name);
	}

	@RequestMapping("/print.do")
	public void print(HttpMethodRequest request, HttpMethodResponse response, String cacheKey) {
		log.info("print.do  start >>\n" + "cacheKey : " + cacheKey);

		// TODO 是否清空之前未打印的数据
		List<TicketTemplate> list = null;
		try {
			list = TicketsData.getTicketsData(cacheKey);

			for (TicketTemplate ticketTemplate : list) {
				log.info(ticketTemplate.toString());
			}

			PrintService ps = new PrintService();
			ps.printTicket(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.write("ok");
	}

	@RequestMapping("/print/status.do")
	public void printStatus(HttpMethodRequest request, HttpMethodResponse response) {
		log.info("/print/status.do  start >>\n");
		PrintService ps = new PrintService();
		ps.getStatus();
		response.write(PrintUtils.StatusDes(ps.getStatus()));
	}

}
