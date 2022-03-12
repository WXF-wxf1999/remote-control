package cn.tomo.puppet.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Properties;

public class Configure {

    private static final String configFileName = "puppet.properties";
    public static final Properties properties = new Properties();

    static {
        try {
            InputStream in = new FileInputStream(configFileName);
            properties.load(in);

        } catch (IOException e) {
            Log.error(e.getMessage());
        }
    }

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
            Log.error(e.getMessage());
        }
        return null;
    }
}




















