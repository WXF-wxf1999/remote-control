package cn.tomo.controller.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Properties;

import cn.tomo.controller.ui.MainActivity;

public class Configure {

    private static final String configFileName = "raw/puppet.properties";
    public static final Properties properties = new Properties();
    private static int sessionId = 0;
    private static final int bufferLength = 1024*1024*10;
    private static MainActivity mainActivity = null;
    private static int puppetScreenWidth = 0;
    private static int getPuppetScreenHeight = 0;
    private static boolean initScreen = false;
    private static int controllerScreenWidth = 0;
    private static int ControllerScreenHeight = 0;

    public static int getControllerScreenWidth() {
        return controllerScreenWidth;
    }

    public static void setControllerScreenWidth(int controllerScreenWidth) {
        Configure.controllerScreenWidth = controllerScreenWidth;
    }

    public static int getControllerScreenHeight() {
        return ControllerScreenHeight;
    }

    public static void setControllerScreenHeight(int getControllerScreenHeight) {
        Configure.ControllerScreenHeight = getControllerScreenHeight;
    }

    public static boolean getInitScreen() {
        return initScreen;
    }

    public static int getPuppetScreenWidth() {
        return puppetScreenWidth;
    }

    public static int getPuppetScreenHeight() {
        return getPuppetScreenHeight;
    }

    public static void setPuppetScreenWidth(int puppetScreenWidth) {
        initScreen = true;
        Configure.puppetScreenWidth = puppetScreenWidth;
    }

    public static void setPuppetScreenHeight(int getPuppetScreenHeight) {
        Configure.getPuppetScreenHeight = getPuppetScreenHeight;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        Configure.mainActivity = mainActivity;
    }

    public static int getBufferLength() {
        return bufferLength;
    }

    public static void initConfig(InputStream in) {
        try {
            properties.load(in);

        } catch (IOException e) {
            Log.i(Configure.class.getName(),e.getMessage());
        }

    }

    public static void showInformation(String info) {
        Bundle bundle = new Bundle();
        bundle.putString("controller", info);
        Message message = Message.obtain();
        message.what = 2;
        message.setData(bundle);
        Configure.getMainActivity().getHandler().sendMessage(message);
    }

    public static void setSessionId(int sessionId) {

        // set highest bit is 1
        final int base = 0x80000000;

        sessionId += base;

        Configure.sessionId = sessionId;
    }

    public static int getSessionId() {return sessionId; }

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("Port"));
    }

    public static String getHost() {
        return properties.getProperty("Host");
    }

    public static String getMacAddress() {

        try {
            InetAddress ia = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i< mac.length; i ++) {
                if(i != 0) {
                    sb.append("-");
                }
                String hexString = Integer.toHexString(mac[i] & 0xFF);
                sb.append(hexString.length() == 1 ? "0" + hexString : hexString);
            }
            return sb.toString().toUpperCase();
        }
        catch (Exception e) {
            Log.e(Configure.class.getName(), e.getMessage());
        }
        return null;
    }
}