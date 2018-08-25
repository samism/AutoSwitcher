package net.samism.java.AutoSwitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class AutoSwitcher extends JFrame {
    private ScreenCapture sc;

    private final static Toolkit tk;
    private final static Dimension screenSize;

    private static final ImageIcon SET_IMAGE;

    private JPanel settingsPanel, setPanel;
    private JTabbedPane tabs;
    private JButton setButton;
    private JTextField coordsField;

    private Container c;

    private Point setPoint = null;

    static {
        SET_IMAGE = new ImageIcon(AutoSwitcher.class.getResource("resources/setButton.png"));

        tk = Toolkit.getDefaultToolkit();
        screenSize = tk.getScreenSize();
    }

    public AutoSwitcher() {
        super("Auto Clicker");

        setWindowProperties();
        initializeComponents();
        doMethods();
        addListeners();
    }

    private void setWindowProperties() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(tk.getScreenSize().width-250, tk.getScreenSize().height-200, 250, 200);
        setAlwaysOnTop(true);
    }

    private void initializeComponents() {
        sc = new ScreenCapture(this);

        tabs = new JTabbedPane();

        setButton = new JButton(SET_IMAGE);

        settingsPanel = new JPanel();
        setPanel = new JPanel();

        coordsField = new JTextField("hit set");

        c = getContentPane();
    }

    private void doMethods() {
        coordsField.setPreferredSize(new Dimension(100, 28));
        setButton.setPreferredSize(new Dimension(SET_IMAGE.getIconWidth(), SET_IMAGE.getIconHeight()));

        coordsField.setEditable(false);

        setPanel.add(coordsField);
        setPanel.add(setButton);

        settingsPanel.add(setPanel, BorderLayout.SOUTH);

        tabs.addTab("Settings", settingsPanel);

        c.add(tabs, BorderLayout.CENTER);
    }

    private void addListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                sc.dispose(); // dispose screenFrame window
                dispose(); // dispose this
                System.exit(0); // terminate jvm¬
            }
        });

        setButton.addActionListener(e -> {
            Image screen = null;

            try {
                screen = new Robot().createScreenCapture(new Rectangle(
                        screenSize));
            } catch (AWTException ex) {
                ex.printStackTrace();
            }

            sc.setImage(screen);
            sc.setVisible(true);
        });
    }

    void setSetPoint(Point p) {
        this.setPoint = p;
    }

    Point getSetPoint() {
        return this.setPoint;
    }

    JTextField getCoordsField() {
        return this.coordsField;
    }

    Dimension getScreenSize() {
        return screenSize;
    }
}