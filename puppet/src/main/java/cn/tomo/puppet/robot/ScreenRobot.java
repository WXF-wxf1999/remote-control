package cn.tomo.puppet.robot;

import cn.tomo.puppet.common.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScreenRobot {

    private static byte[] lastScreenData = null;
    private static BufferedImage cursor = null;

    static {
        try {
            cursor = ImageIO.read(new File("cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getScreen() {

        while (true) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {

                // get screen data
                BufferedImage screenCapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                Point p= MouseInfo.getPointerInfo().getLocation();
                screenCapture.createGraphics().drawImage(cursor, p.x, p.y, null);
                ImageIO.write(screenCapture, "png", byteArrayOutputStream);

            } catch (AWTException | IOException e) {
                Log.error(e.toString());
            }

            byte[] screenData = byteArrayOutputStream.toByteArray();
            // if it has changed
            if(Arrays.equals(screenData, lastScreenData)) {
                continue;
            }
            lastScreenData = screenData;
            return screenData;
        }
    }


}
