package com.print;

import com.print.Main.TscLibDll;

/**
 * description:
 * 
 * @author don
 * @date 2016年6月15日 下午5:47:39
 *
 */
public class PrintUtils {

	public static void setUp() {
		TscLibDll.INSTANCE.openport("TSC T-300A");
		TscLibDll.INSTANCE.setup("80", "194", "6", "6", "1", "6", "0");
	}

	public static void close() {
		TscLibDll.INSTANCE.closeport();
	}
}
