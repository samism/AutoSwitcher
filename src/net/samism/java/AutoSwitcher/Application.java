package net.samism.java.AutoSwitcher;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Application {
    AutoSwitcher autoSwitcher;

    private Application() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());

        SwingUtilities.invokeLater(() -> {
            autoSwitcher = new AutoSwitcher();
            autoSwitcher.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new Application();
    }

    private class GlobalKeyListenerExample implements NativeKeyListener {
        Robot robot;

        GlobalKeyListenerExample() {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
            if(nativeKeyEvent.getKeyChar() == '`') {
                robot.mouseMove(autoSwitcher.getSetPoint().x, autoSwitcher.getSetPoint().y);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                System.out.println(nativeKeyEvent.getKeyCode());
            }
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

        }
    }
}