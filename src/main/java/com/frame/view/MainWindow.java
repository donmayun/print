package com.frame.view;

import static com.frame.utils.FrameConstant.FRAME_HEIGHT;
import static com.frame.utils.FrameConstant.FRAME_TITLE;
import static com.frame.utils.FrameConstant.FRAME_WIDTH;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.frame.decorator.MenuDecorator;

/**
 * description: 主界面
 * 
 * @author don
 * @date 2016年6月8日 上午11:49:51
 *
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainWindow(int windowWidth, int windwoHeight) {
		super(FRAME_TITLE);
		this.setSize(windowWidth, windwoHeight);

	}

	public MainWindow() {
		super(FRAME_TITLE);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	public void initWindow() {
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setIcon();

		new MenuDecorator(this).addMenu();
		
		this.setVisible(true);
	}

	/**
	 * description: 设置icon
	 *
	 *
	 * @author don
	 * @date 2016年6月8日 下午3:03:18
	 */
	private void setIcon() {
		ImageIcon imageIcon = new ImageIcon("./src/main/resouces/imgs/icon.jpg");
		this.setIconImage(imageIcon.getImage());
	}

}
