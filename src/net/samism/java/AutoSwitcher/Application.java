package net.samism.java.AutoSwitcher;

import javax.swing.SwingUtilities;


/**
 *
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 *
 * @author Sameer
 *         <p/>
 *         <p/>
 *         This class contains the main method of the program, and through it
 *         creates the main UI of the program through the Event Dispatch Thread.
 *         The main UI ({@link AutoSwitcher}) has two other UIs,
 *         {@link ScreenCapture}, and {@link MiniWindow}. ScreenCapture is meant
 *         to replicate the users screen with a screenshot so that the user can
 *         click on a designated point on there screen in order for me to grab
 *         the coordinates that I need. I use these coordinates as a relative
 *         point so that I know where to aim for, when I later use the Robot
 *         class to click on the user's inventory slots. MiniWindow is meant to
 *         be a compact, simple window inteded for the user so that he/she can
 *         quickly click on during a fight.
 *         <p/>
 *         ScreenCapture and MiniWindow are both constructed of high level
 *         contaniners (JFrame) and are the children of the main UI,
 *         AutoSwitcher. Since they are AutoSwitcher's children, they are
 *         constructed under the Event Dispatch Thread as well.
 *         <p/>
 *         ScreenCapture is meant to be an Undecorated Window, MiniWindow is
 *         not.
 *         <p/>
 *         AutoSwitcher is undecorated because the SeaGlass Look And Feel does
 *         not decorate the window frame the same way a skin does.
 *         <p/>
 *         {@link javax.swing.SwingUtilities#invokeLater(Runnable)} is the Swing-specific
 *         method for calling {@link java.awt.EventQueue#invokeLater(Runnable)}.
 */

 /*
    TODO:

    MiniWindow:
        -make slightly smaller (fit in chatbox)
        -make it nicer looking (font, etc)
        -change back image
        -make the top left black space above back button transparent
        -make the window move while dragging


    Autoswitcher:
        -develop a way to save the current laf, relative point, and slots
        -make the window move while dragging

  */

public class Application {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AutoSwitcher().setVisible(true);
			}
		});
	}
}