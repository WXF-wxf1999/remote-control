package cn.tomo.puppet.robot;

import java.awt.*;

public class CursorRobot {

    Robot robot = new Robot();

    private static class CursorRobotHolder{

        private static CursorRobot instance = null;
        static {
            try {
                instance = new CursorRobot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public CursorRobot() throws AWTException {
    }

    public static CursorRobot getInstance(){
        return CursorRobotHolder.instance;
    }

    public void mouseEvent(int x, int y, int keyCode) {

        robot.mouseMove(x, y);
        robot.mousePress(keyCode);
        robot.delay(100);
        robot.mouseRelease(keyCode);

    }

    public void mouseWheel(int distance) {

        // what?
        robot.mouseWheel(distance);
    }


}
