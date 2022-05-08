package cn.tomo.puppet.robot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyboardRobot {

     Robot robot = new Robot();

    public KeyboardRobot() throws AWTException {
    }

    private static class KeyBoardRobotHolder{

        private static KeyboardRobot instance = null;
        static {
            try {
                instance = new KeyboardRobot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public static KeyboardRobot getInstance(){
        return KeyboardRobot.KeyBoardRobotHolder.instance;
    }

    private void keyPress(int keyCode) {

        robot.keyPress(keyCode);
        robot.delay(50);
        robot.keyRelease(keyCode);
    }

    public void keyPress(byte[] key) {

        String Key = new String(key);

        if(Key.equals("ENTER")) {

            keyPress(KeyEvent.VK_ENTER);
        } else if(Key.equals("DELETE")) {

            keyPress(KeyEvent.VK_BACK_SPACE);
        } else {

            // judge case : upper case letter
            int keyValue = Key.charAt(0);
            if(keyValue >= (int)'A' && keyValue <= (int)'Z' ) {
                keyPress(KeyEvent.VK_CAPS_LOCK);
                keyPress(keyValue);
                keyPress(KeyEvent.VK_CAPS_LOCK);
                return;
            }
            keyPress(keyValue + (int)'A' - (int)'a');
        }

    }

}
