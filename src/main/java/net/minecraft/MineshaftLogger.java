package net.minecraft;

import org.apache.logging.log4j.LogManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MineshaftLogger {

    public static void logDebug(String message) {
        LogManager.getLogger().info("Mineshaft Debug: {}", message);
    }
    public static void logError(String message) {
        LogManager.getLogger().error("Mineshaft Error: {}", message);
    }

}
