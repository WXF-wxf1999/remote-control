package cn.tomo.puppet.robot;

import cn.tomo.puppet.common.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DeviceRobot {

    private static byte[] lastScreenData = null;
    public static byte[] getScreen() {

        while (true) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {

                // get screen data
                BufferedImage screenCapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

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
