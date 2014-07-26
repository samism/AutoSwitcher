package net.samism.java.AutoSwitcher;

/**
 *
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 *
 * @author Sameer
 * <p/>
 * <p/>
 * This class is a more compact and simple version of main UI. It is meant to be very straight-forward
 * in terms of use since the most crucial times for the user to take advantage of this program's functionality
 * are usually in times where time is very limited.
 *
 * It has a "go" button, which starts the functionality of the Robot class in order to move the mouse, and
 * generate mouse-click events, as well as a "back" button, made so that the user can switch back to the main
 * UI if he/she would like to make modifications to the inventory slots that he/she wants to have clicked, as well
 * as to make any changes in the main settings.
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MiniWindow extends JFrame {

	private final MiniWindow mw = this;
	private final AutoSwitcher as;
	private Robot r;

	private static final ImageIcon BACK_IMAGE;

	private final Rectangle SWITCH_BUTTON_AREA = new Rectangle(55, 2, 190, 170);
	private final Rectangle BACK_BUTTON_AREA = new Rectangle(2, 148, 50, 25);

	private Rectangle[] slots = null;
	private boolean[] slotFlags = null;

	static {
		BACK_IMAGE = new ImageIcon(MiniWindow.class.getResource("resources/backButton.png"));
	}

	public MiniWindow(AutoSwitcher as) {
		super("AutoSwitch");

		this.as = as;

		MiniWindowPanel panel = new MiniWindowPanel();
		Dimension screenSize = as.getScreenSize();

		try {
			this.r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		setIconImage(as.getIconImage());
		setBounds(2, (int) (screenSize.getHeight() - 501 - this.getHeight()), 250, 175);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);

		panel.addMouseListener(panel);

		getContentPane().add(panel);
	}

	class MiniWindowPanel extends JPanel implements MouseListener {

		int x = 0;
		int y = 0;

		boolean dragging = false;

		public MiniWindowPanel() {
			super();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHints(new RenderingHints(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setColor(Color.YELLOW);
			g.setFont(new Font("Helvetica", Font.PLAIN, 45));

			g.drawRect(55, 2, 190, 170);
			g.drawString("SWITCH", (175 / 2) - 25, 100);

			g.drawImage(BACK_IMAGE.getImage(), 2, 148, 50, 25, null);
		}

		public void mousePressed(MouseEvent e) {
			Point click = e.getPoint(); //0, 0 starts at gui top-left
			Point onScreen = e.getLocationOnScreen(); //0, 0 starts at screen top-left

			Point setPoint = as.getSetPoint();
			boolean returnMouse = as.getReturnToMouseCoordsBox().isSelected();

			boolean isFirst = true;

			if (SWITCH_BUTTON_AREA.contains(click)) {
				if (hasAtLeastOneSlotClicked(slotFlags) && setPoint != null) {
					for (int i = 0; i < slotFlags.length; i++) {
						if (slotFlags[i]) {
							//calibrated:
							//slot.x + setPoint.x - relative calculation + half of 1 slot to target center
							//slot.y + setPoint.y - relative calculation + half of 1 slot to target center
							r.mouseMove((slots[i].x + setPoint.x - 145 + (28 / 2)), //x
									(slots[i].y + setPoint.y - 25 + (28 / 2))); //y

							//if slotFlags[i] is the first-most slot, mousePress & release to deal
							//with focus issue
							if (isFirst) {
								r.mousePress(InputEvent.BUTTON1_MASK);
								r.mouseRelease(InputEvent.BUTTON1_MASK);
								isFirst = false;
							}
							
							r.mousePress(InputEvent.BUTTON1_MASK);
							r.mouseRelease(InputEvent.BUTTON1_MASK);
							mw.sleep();
						}
					}
				} else {
					boolean slotFlagsBool = hasAtLeastOneSlotClicked(slotFlags);
					boolean setPointBool = (setPoint != null);
					JOptionPane.showMessageDialog(mw,
							"You set at least 1 slot to be clicked: "
									+ slotFlagsBool
									+ "\nYou set the relative point: "
									+ setPointBool
					);
				}
				if (returnMouse) {
					r.mouseMove(onScreen.x, onScreen.y);
				}
			} else if (BACK_BUTTON_AREA.contains(click)) {
				mw.setVisible(false);
				as.setVisible(true);
			} else {
				dragging = true;
				x = mw.getX() - e.getX();
				y = mw.getY() - e.getY();
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (dragging) {
				int newx = x + e.getX();
				int newy = y + e.getY();

				mw.setLocation(new Point(newx, newy));
				dragging = false;
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	private void sleep() {
		try {
			Thread.sleep((long) 25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean hasAtLeastOneSlotClicked(boolean[] slots) {
		boolean hasClickedSlots = false;
		for (boolean slot : slots) {
			if (slot) {
				hasClickedSlots = true;
				break;
			}
		}
		return hasClickedSlots;
	}

	public void setSlots(Rectangle[] slots) {
		this.slots = slots;
	}

	public void setSlotFlags(boolean[] flags) {
		this.slotFlags = flags;
	}
}