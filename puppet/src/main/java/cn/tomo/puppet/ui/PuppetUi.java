package cn.tomo.puppet.ui;

import javax.swing.*;
import java.awt.*;

public class PuppetUi {

    private static final JFrame frame = new JFrame("Puppet");
    private static final Dialog dialog = new Dialog();

    static {
        frame.setContentPane(dialog.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showDialog(boolean is) {
        frame.setVisible(is);
    }

    public static void setSessionId(int sessionId) {

        dialog.getSessionIdArea().setText(String.valueOf(sessionId));
    }

    public static void setState(String stateInfo) {

        dialog.getStateArea().setText(stateInfo);
    }

}
