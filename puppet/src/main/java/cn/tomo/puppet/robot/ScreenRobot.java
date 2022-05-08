package cn.tomo.puppet.robot;

import cn.tomo.puppet.common.Configure;
import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;

import java.awt.*;

public class ScreenRobot {

    private static final Pointer buffer = new Memory(Configure.getBufferLength());

    static {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        StdCallDll.INSTANCE.setScreenSize(dimension.width,dimension.height);
    }
    public static byte[] getScreen() {

        int len = StdCallDll.INSTANCE.screenCapture(buffer);
        return buffer.getByteArray(0,len);
    }

    public interface StdCallDll extends StdCallLibrary {

        StdCallDll INSTANCE = (StdCallDll) Native.loadLibrary("./lib/screen_helper", StdCallDll.class);// 加载动态库文件

        void setScreenSize(int width, int height);
        int screenCapture(Pointer buffer);
    }
}





//
//public class ScreenRobot {
//
//    private static byte[] lastScreenData = null;
//    private static byte[] curScreenData = null;
//    private static BufferedImage cursor = null;
//    private static Robot robot = null;
//    private static Rectangle rectangle = null;
//    static {
//        try {
//            robot = new Robot();
//            rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//        try {
//            cursor = ImageIO.read(new File("cursor.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static byte[] getScreen() {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        while (true) {
//            try {
//                // get screen dat
//                BufferedImage screenCapture = new Robot().createScreenCapture(rectangle);
//                Point p= MouseInfo.getPointerInfo().getLocation();
//                screenCapture.createGraphics().drawImage(cursor, p.x, p.y, null);
//                ImageIO.write(screenCapture, "png", byteArrayOutputStream);
//
//            } catch (AWTException | IOException e) {
//                Log.error(e.toString());
//            }
//
//            byte[] screenData = byteArrayOutputStream.toByteArray();
//            byteArrayOutputStream.reset();
//            // if it has changed
//            if(Arrays.equals(screenData, lastScreenData)) {
//                continue;
//            }
//            lastScreenData = screenData;
//            return screenData;
//        }
//    }
//
//    public static byte[] getScreen() {
//
//        if(curScreenData != null) {
//
//            return curScreenData;
//        }
//        // start task
//        ExecutorService exec = Executors.newCachedThreadPool();
//        for(int i = 0; i < 2; i++){
//
//            exec.execute(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    byte[] lastScreenData = null;
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    while (true) {
//                        try {
//                            // get screen data
//                            BufferedImage screenCapture = robot.createScreenCapture(rectangle);
//                            Point p= MouseInfo.getPointerInfo().getLocation();
//                            screenCapture.createGraphics().drawImage(cursor, p.x, p.y, null);
//                            ImageIO.write(screenCapture, "png", byteArrayOutputStream);
//
//                        } catch (IOException e) {
//                            Log.error(e.toString());
//                        }
//
//                        byte[] screenData = byteArrayOutputStream.toByteArray();
//                        byteArrayOutputStream.reset();
//                        // if it has changed
//                        if(Arrays.equals(screenData, lastScreenData)) {
//                            continue;
//                        }
//                        lastScreenData = screenData;
//                        curScreenData = screenData;
//                    }
//                }
//            });
//        }
//        return getScreen();
//    }
//

//}
