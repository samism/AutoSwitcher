package com.samism.rs.autoswitcher;

import javax.swing.*;
import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 *
 * @author Sameer
 *         <p/>
 *         <p/>
 *         Component that goes into the inventory slot selection tab
 *         <p/>
 *         allows user to designate which inventory slots the program will
 *         move the mouse to and click
 */

class InventoryPanel extends JPanel {

	private boolean[] slotFlags;
	private final Rectangle[] slots;
	private final static ImageIcon INV, CHECK;

	private boolean gotSlots;

	static {
		INV = new ImageIcon(InventoryPanel.class.getResource("inventory.png"));
		CHECK = new ImageIcon(InventoryPanel.class.getResource("check.png"));
	}

	public InventoryPanel() {
		this.slotFlags = new boolean[28]; //defaults to false
		this.slots = new Rectangle[28];
		this.gotSlots = false;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(INV.getImage(), 9, 14, null);
		g.setColor(Color.yellow);

		if (!gotSlots) {
			int xval = 45; //start x here
			int yval = 55; //start y here

			//load array with the slots
			//dont modify
			for (int i = 0; i <= 27; i++) {
				slots[i] = new Rectangle(xval, yval, 28, 28);
				yval += 37;
				if (i == 6 || i == 13 || i == 20) {
					xval += 45;
					yval = 55;
				}
			}
			gotSlots = true;
		}

		//find slots clicked, and mark with check
		//necessary functionality
		for (int i = 0; i < slotFlags.length; i++) {
			if (slotFlags[i]) {
				g.drawImage(CHECK.getImage(), slots[i].x, slots[i].y, null);
			}
		}

//		//draw the slots
//		for(int i = 0; i <= 27; i++){
//			g.drawRect(slots[i].x, slots[i].y, 28, 28);
//		}
//		
//		//draw the indices
//		for(int i = 0; i <= 27; i++){
//			g.drawString(Integer.toString(i), slots[i].x + 10, slots[i].y + 15);
//		}
	}

	public Rectangle[] getSlots() {
		return slots;
	}

	public boolean[] getSlotFlags() {
		return slotFlags;
	}

	public void setSlotFlags(boolean[] flags) {
		this.slotFlags = flags;
	}
}
