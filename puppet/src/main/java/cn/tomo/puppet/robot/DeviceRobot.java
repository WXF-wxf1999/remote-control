package cn.tomo.puppet.robot;

import cn.tomo.puppet.common.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeviceRobot {
    public static byte[] getScreen() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            // get screen data
            BufferedImage screenCapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

            ImageIO.write(screenCapture, "png", byteArrayOutputStream);

        } catch (AWTException | IOException e) {
            Log.error(e.toString());
        }
        return byteArrayOutputStream.toByteArray();
    }

}
