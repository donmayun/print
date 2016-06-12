package com.frame.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * description: 
 * @author don
 * @date 2016年6月8日 下午5:06:30
 *
 */
public class ItemListener implements ActionListener {

	/**
	 * description: 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());

	}

}
