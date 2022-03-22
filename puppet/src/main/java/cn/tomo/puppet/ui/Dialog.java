package cn.tomo.puppet.ui;

import javax.swing.*;

public class Dialog {
    private JPanel panel;
    private JTextArea sessionIdDescription;
    private JTextArea sessionId;
    private JTextArea state;

    public JPanel getPanel() {
        return panel;
    }

    public JTextArea getSessionIdArea() {
        return sessionId;
    }

    public JTextArea getStateArea() {
        return state;
    }

}
