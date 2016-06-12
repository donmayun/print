package com.frame.decorator;

import static com.frame.utils.FrameConstant.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.exception.ErrorCode;
import com.exception.FrameException;
import com.frame.listener.ItemListener;

/**
 * description: 给窗体装饰菜单
 * 
 * @author don
 * @date 2016年6月8日 下午3:54:27
 *
 */
public class MenuDecorator {

	private JFrame jFrame;
	private JMenuBar jMenuBar;

	/**
	 * @param jFrame
	 */
	public MenuDecorator(JFrame jFrame) {
		this.jFrame = jFrame;
		this.jMenuBar = new JMenuBar();
	}

	public void addMenu() {
		validateFrame();
		
		JMenu userM = new JMenu(MENU_1);
		JMenuItem menuItem = new JMenuItem(MENU_1_ITEM_1);
		menuItem.addActionListener(new ItemListener());
		userM.add(menuItem);
		userM.add(new JMenuItem(MENU_1_ITEM_2));

		JMenu ticketM = new JMenu(MENU_2);
		ticketM.add(new JMenuItem(MENU_2_ITEM_1));
		ticketM.add(new JMenuItem(MENU_2_ITEM_2));
		
		JMenu setM = new JMenu(MENU_3);
		setM.add(new JMenuItem(MENU_3_ITEM_1));
		
		
		
		jMenuBar.add(userM);
		jMenuBar.add(ticketM);
		jMenuBar.add(setM);

		jFrame.setJMenuBar(jMenuBar);
	}

	/**
	 * description: 验证窗体和菜单栏
	 *
	 *
	 * @author don
	 * @date 2016年6月8日 下午4:25:09
	 */
	private void validateFrame() {
		if (jFrame == null) {
			throw FrameException.Error(ErrorCode.ERROR_NULL_FRAME);
		}
		if (jMenuBar == null) {
			throw FrameException.Error(ErrorCode.ERROR_NULL_MENUBAR);
		}
	}
}
