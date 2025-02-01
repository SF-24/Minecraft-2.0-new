package net.minecraft;
import java.io.File;
import java.util.Arrays;

import net.minecraft.client.main.Main;

/**
 * Welcome to MCP 1.8.9 for Maven
 * This repository has been created to make working with MCP 1.8.9 easier and cleaner.
 * You can view the MCP 1.8.9 repo here: https://github.com/Marcelektro/MCP-919
 * If you have any questions regarding this, feel free to contact me here: https://marcloud.net/discord
 *
 * Have fun with the MC development ^^
 * Marcelektro
 */

public class Run {
    public static void main(String[] args) {
        // Provide natives
        // Currently supported Linux and Windows
        System.setProperty("org.lwjgl.librarypath", new File("../test_natives/" + (System.getProperty("os.name").startsWith("Windows") ? "windows" : "linux")).getAbsolutePath());

        Main.main(concat(new String[]{"--version", "MavenMCP", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}", /*"--username", "XpKitty", "--uuid", "7e8a85ee3bb04158a1075a6886ffe438"*/}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
