package com.samism.rs.autoswitcher;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 *
 *
 * @author Sameer
 * </p>
 * </p>
 * This class is meant as the main UI of the program. It is where all the other objects and such are centered through.
 *
 * When it is constructed, it constructs two other "worker" UIs, one to help grab screen coordinates, and one
 * for the user to use conveniently, in a compact fashion.
 *
 * This UI has three tabs, one where the user can designate the inventory slots he/she wants clicked during the 
 * switch, one tabs with settings the user can change as well the necessary "set" button the user needs to use
 * in order for the program to function properly. The program's core functionality relies on the user setting
 * the correct coordinates using this button to work. The last tab is for information about the program, as well
 * as a "donate" button for anyone who would like to donate any amount of money to the author of this program.
 *
 * The last component on this UI is the "start" button, which when pressed allows this UI to go invisible,
 * so that the other, more compact UI can stand ready for the user to invoke.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

class AutoSwitcher extends JFrame {

	private final AutoSwitcher as = this;
	private MiniWindow mw;
	private ScreenCapture sc;

	private final static Toolkit tk;
	private final static Dimension screenSize;

	private static final ImageIcon ICON_IMAGE, DONATE_IMAGE, SET_IMAGE,
			CLOSE_IMAGE, MINIMIZE_IMAGE;

	private InventoryPanel inventoryPanel;

	private JPanel settingsPanel, startButtonPanel, creditsPanel, topPanel,
			regularSettingsPanel, setPanel;
	private JTabbedPane tabs;
	private JButton startButton, donateButton, closeButton, minimizeButton,
			setButton, themeButton;
	private JCheckBox returnToMouseCordsBox, saveSettingsBox;
	private JTextField coordsField;
	private JTextArea creditsArea;

	private final String os_name = System.getProperty("os.name").toLowerCase();
	private final HashMap<String, Boolean> settings = new HashMap<String, Boolean>();

	private Container c;

	private Rectangle[] slots;

	private Point setPoint = null;

	static {
		ICON_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("spitfire.png"));
		DONATE_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("donate.png"));
		SET_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("setButton.png"));
		CLOSE_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("closeButton.png"));
		MINIMIZE_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("minimizeButton.png"));

		tk = Toolkit.getDefaultToolkit();
		screenSize = tk.getScreenSize();
	}

	public AutoSwitcher() {
		super("AutoSwitch");

		try {
			UIManager
					.setLookAndFeel(os_name.contains("windows") ? "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
							: os_name.contains("linux") ? "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
							: UIManager
							.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setWindowProperties();
		initializeComponents();
		doMethods();
		addListeners();
	}

	void setWindowProperties() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(2, (int) (screenSize.getHeight() - 501 - getHeight()), 260,
				455);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setIconImage(ICON_IMAGE.getImage());
	}

	void initializeComponents() {
		mw = new MiniWindow(this);
		sc = new ScreenCapture(this);

		inventoryPanel = new InventoryPanel();

		tabs = new JTabbedPane();

		startButton = new JButton("START");
		donateButton = new JButton(DONATE_IMAGE);
		closeButton = new JButton(CLOSE_IMAGE);
		minimizeButton = new JButton(MINIMIZE_IMAGE);
		setButton = new JButton(SET_IMAGE);
		themeButton = new JButton("Change theme");

		settingsPanel = new JPanel();
		startButtonPanel = new JPanel();
		creditsPanel = new JPanel();
		topPanel = new JPanel();
		setPanel = new JPanel();
		regularSettingsPanel = new JPanel();

		returnToMouseCordsBox = new JCheckBox("Return mouse?", false);
		saveSettingsBox = new JCheckBox("Save settings?", true);

		coordsField = new JTextField("hit set");
		creditsArea = new JTextArea();

		c = getContentPane();

		slots = inventoryPanel.getSlots();
	}

	void doMethods() {
		// 1. main gui frame + panel
		// 2. screenCapture frame + panel
		// 4. MiniWindow frame + panel

		// main gui stuff
		coordsField.setPreferredSize(new Dimension(100, 28));
		donateButton.setPreferredSize(new Dimension(DONATE_IMAGE.getIconWidth(), DONATE_IMAGE.getIconHeight()));
		setButton.setPreferredSize(new Dimension(SET_IMAGE.getIconWidth(), SET_IMAGE.getIconHeight()));

		themeButton.setToolTipText("Change theme");
		coordsField.setEditable(false);

		// top panel
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(closeButton, BorderLayout.NORTH);
		topPanel.add(minimizeButton, BorderLayout.NORTH);

		topPanel.setBackground(Color.BLACK);

		// settings panel stuff
		// regularSettingPanel + setPanel = settingsPanel
		regularSettingsPanel.setLayout(new BoxLayout(regularSettingsPanel,
				BoxLayout.Y_AXIS));
		regularSettingsPanel.add(returnToMouseCordsBox);
		//regularSettingsPanel.add(saveSettingsBox);
		regularSettingsPanel.add(themeButton);

		setPanel.add(coordsField);
		setPanel.add(setButton);

		settingsPanel.add(regularSettingsPanel, BorderLayout.NORTH);
		settingsPanel.add(setPanel, BorderLayout.SOUTH);

		// creditsPanel stuff
		creditsArea.setLineWrap(true);
		creditsArea.setEditable(false);
		creditsArea.setWrapStyleWord(true);
		creditsArea.setPreferredSize(new Dimension(225, 180));
		creditsArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
		creditsArea
				.setText("Runescape AutoSwitcher\n\nWritten by: TheLeadingFire\n\n"
						+ "Instructions are in the README.txt file.\n\n"
						+ "When you are ready, click start.");

		creditsPanel.add(creditsArea, BorderLayout.CENTER);
		creditsPanel.add(donateButton, BorderLayout.SOUTH);

		// start button panel
		startButtonPanel.add(startButton);
		startButtonPanel.setBackground(Color.BLACK);

		// tabs
		tabs.addTab("Inventory", inventoryPanel);
		tabs.addTab("Settings", settingsPanel);
		tabs.addTab("About", creditsPanel);

		// main gui adding
		c.add(topPanel, BorderLayout.NORTH);
		c.add(tabs, BorderLayout.CENTER);
		c.add(startButtonPanel, BorderLayout.SOUTH);
	}

	void addListeners() {
		startButton.addActionListener(new ActionListener() {


			final boolean[] slotFlags = inventoryPanel.getSlotFlags();
			final Rectangle[] slots = inventoryPanel.getSlots();

			public void actionPerformed(ActionEvent evt) {
				// save individual options
				if (returnToMouseCordsBox.isSelected()) {
					settings.put("restore_mouse", true);
				}

				if (saveSettingsBox.isSelected()) {
					settings.put("save", true);
				}

				if (((!UIManager.getLookAndFeel().getName().equals("Windows")) && (os_name.contains("windows"))) ||
						((UIManager.getLookAndFeel().getName().contains("GTK") && (os_name.contains("linux"))))) {
					settings.put(UIManager.getLookAndFeel().getClass().getCanonicalName(), true);
				}

				mw.setSlots(slots);
				mw.setSlotFlags(slotFlags);

				setVisible(false);
				mw.setVisible(true);
			}
		});

		inventoryPanel.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent evt) {
				// get updated slot flags every click
				boolean[] slotFlags = inventoryPanel.getSlotFlags();
				for (int i = 0; i <= 27; i++) {
					// if clicked in slot, set status of the slot to opposite of
					// what it was
					if (slots[i].contains(evt.getPoint())) {
						slotFlags[i] = !slotFlags[i];
					}
				}
				inventoryPanel.setSlotFlags(slotFlags);
				inventoryPanel.repaint(); // update the slots if clicked or not
				// (necessary)
			}

			public void mouseClicked(MouseEvent evt) {
			}

			public void mouseEntered(MouseEvent evt) {
			}

			public void mouseExited(MouseEvent evt) {
			}

			public void mouseReleased(MouseEvent evt) {
			}
		});

		donateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// if browser supported, open donate link in default browser
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop()
								.browse(new URI(
										"https://www.paypal.com/cgi-bin/webscr?"
												+ "cmd=_donations"
												+ "&business=3XHMHZWDYNFUW"
												+ "&lc=US"
												+ "&item_name=TheLeadingFire"
												+ "&currency_code=USD"
												+ "&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// clean exit
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mw.dispose(); // dispose mini window
				sc.dispose(); // dispose screenFrame window
				dispose(); // dispose this
				System.exit(0); // terminate jvm
			}
		});

		// minimize window
		minimizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});

		// for repositioning window
		topPanel.addMouseListener(new MouseListener() {
			int lastx = 0;
			int lasty = 0;

			public void mousePressed(MouseEvent evt) {
				lastx = as.getX() - evt.getX();
				lasty = as.getY() - evt.getY();
			}

			public void mouseReleased(MouseEvent evt) {
				int newx = lastx + evt.getX();
				int newy = lasty + evt.getY();

				as.setLocation(new Point(newx, newy));
			}

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});

		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Image screen = null;
				try {
					screen = new Robot().createScreenCapture(new Rectangle(
							screenSize));
				} catch (AWTException ex) {
					ex.printStackTrace();
				}
				sc.setImage(screen);
				sc.setVisible(true);
			}
		});

		themeButton.addActionListener(new ActionListener() {
			final String[] themeList = new String[]{
					"org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceEmeraldDuskLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceChallengerDeepLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceOfficeBlack2007LookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel",
					"org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel",
					"javax.swing.plaf.metal.MetalLookAndFeel",
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"};

			int i = 0;

			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(themeList[i]);
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				} catch (UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				} catch (InstantiationException ex) {
					ex.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(as);
				themeButton.setToolTipText(UIManager.getLookAndFeel().getName());
				i = (i == themeList.length - 1 ? 0 : i + 1);
			}
		});
	}

//	public void loadSettings(Settings stngs) {
//		this.setTitle("Loading settings from file");
//		// this.saveSettingsBox.setSelected(stngs)
//		// ...
//		this.setTitle("AutoSwitcher");
//	}
//
//	public Dimension getScreenSize() {
//		return screenSize;
//	}

	public Image getIconImage() {
		return ICON_IMAGE.getImage();
	}

	public void setSetPoint(Point p) {
		this.setPoint = p;
	}

	public Point getSetPoint() {
		return this.setPoint;
	}

	public JTextField getCoordsField() {
		return this.coordsField;
	}

	public JCheckBox getReturnToMouseCoordsBox() {
		return this.returnToMouseCordsBox;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}
}