package cn.tomo.puppet.common;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {

    private static final String logFileName = "puppet.log";
    private static final Logger logger = Logger.getLogger(logFileName);

    static {
        try {
            // initialize the logger
            FileHandler fileHandler = new FileHandler(logFileName);
            fileHandler.setFormatter(new innerFormat());
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(Supplier<String> msgSupplier) {
        logger.info(msgSupplier);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static void warning(Supplier<String> msgSupplier) {
        logger.warning(msgSupplier);
    }

    public static void error(String msg) {
        logger.severe(msg);
    }

    public static void error(Supplier<String> msgSupplier) {
        logger.severe(msgSupplier);
    }

    static class innerFormat extends Formatter {
        @Override
        public String format(LogRecord record) {

            ZonedDateTime time =ZonedDateTime.now();
            String timeFormat  = time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            return "["+ record.getLevel() + "]" + " ["+ timeFormat + "]: " + record.getMessage() +"\n";
        }
    }

}
