package net.minecraft;

import net.minecraft.server.dedicated.DedicatedServer;

import java.io.File;

public class MainServer
{
    public static void main(String[] p_main_0_)
    {
        net.minecraft.init.Bootstrap.register();

        try
        {
            boolean flag = true;
            String s = null; // Server Owner (singleplayer name)
            String s1 = "."; // Game Dir
            String s2 = null; // World Name
            boolean flag1 = false; // Demo Mode
            boolean flag2 = false; // Bonus Chest
            int i = -1; // Port

            // Parse standard server arguments manually or via loop
            for (int j = 0; j < p_main_0_.length; ++j)
            {
                String s3 = p_main_0_[j];
                String s4 = j == p_main_0_.length - 1 ? null : p_main_0_[j + 1];

                if ("--port".equals(s3) && s4 != null)
                {
                    i = Integer.parseInt(s4);
                    ++j;
                }
                else if ("--singleplayer".equals(s3) && s4 != null)
                {
                    s = s4;
                    ++j;
                }
                else if ("--universe".equals(s3) && s4 != null)
                {
                    s1 = s4;
                    ++j;
                }
                else if ("--world".equals(s3) && s4 != null)
                {
                    s2 = s4;
                    ++j;
                }
                else if ("--demo".equals(s3))
                {
                    flag1 = true;
                }
                else if ("--bonusChest".equals(s3))
                {
                    flag2 = true;
                }
                else if ("--nogui".equals(s3))
                {
                    flag = false;
                }
            }

            // Initialize the Dedicated Server
            final DedicatedServer dedicatedserver = new DedicatedServer(new File(s1));

            if (s != null)
            {
                dedicatedserver.setServerOwner(s);
            }

            if (s2 != null)
            {
                dedicatedserver.setFolderName(s2);
            }

            if (i >= 0)
            {
                dedicatedserver.setPort(i);
            }

            if (flag1)
            {
                dedicatedserver.setDemo(true);
            }

            if (flag2)
            {
                dedicatedserver.canCreateBonusChest(true);
            }

            // Disable GUI if requested or if headless
            dedicatedserver.setGuiEnabled(flag && !java.awt.GraphicsEnvironment.isHeadless());

            // Start the server thread
            dedicatedserver.startServerThread();

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread("Server Shutdown Thread")
            {
                public void run()
                {
                    dedicatedserver.stopServer();
                }
            });
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static boolean isNullOrEmpty(String str)
    {
        return str != null && !str.isEmpty();
    }
}
