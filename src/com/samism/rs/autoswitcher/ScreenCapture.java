package com.samism.rs.autoswitcher;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 *
 * @author Sameer
 * <p/>
 * <p/>
 * This class is designed only to feature a screenshot of the user's current screen, and to collect
 * the coordinates from a mousePressed event. The functionality of this class is crucial to the core
 * functionality of the entire program. 
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class ScreenCapture extends JFrame {

	private final AutoSwitcher as;

	private final ScreenCapture sc = this;

	private Point setPoint;
	private Image screen;

	public ScreenCapture(AutoSwitcher as) {
		super();

		this.as = as;
		this.setPoint = null;

		setUndecorated(true);
		setBounds(0, 0, as.getScreenSize().width, as.getScreenSize().height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);

		ScreenCapturePanel scp = new ScreenCapturePanel();
		scp.addMouseListener(scp);

		getContentPane().add(scp);
	}

	public void setImage(Image i) {
		this.screen = i;
	}

	class ScreenCapturePanel extends JPanel implements MouseListener {
		public ScreenCapturePanel() {
			super();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(screen, 0, 0, null);
		}

		public void mouseClicked(MouseEvent e) {
			sc.setPoint = e.getPoint();
			as.setSetPoint(setPoint);
			as.getCoordsField().setText("(" + setPoint.x + ", " + setPoint.y + ")");
			sc.setVisible(false);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}
