package net.mineshaft.data;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;

import java.io.*;

public class UserCacheJsonManager {
//
//    public UserCacheJsonManager() {
//        try {
//            initialiseFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void initialiseFile() {
//        // TODO: load custom capes
//        try {
//            File playerNameFile = getFile();
//        } catch (NullPointerException e) {
//            System.out.println("Error! Cannot find mineshaft data. Skipping");
//        }
//    }
//
//    public static File getFile() {
//        if(!Minecraft.getMinecraft().mcDataDir.exists()) {
//            Minecraft.getMinecraft().mcDataDir.mkdirs();
//        }
//
//        File userFile = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "mineshaft_data", "user_cache.json");
//
//        if (!userFile.exists() || !userFile.getParentFile().exists()) {
//            makeNewFile(userFile);
//        }
//        return userFile;
//    }
//
//    public static void makeNewFile(File file) {
//        try {
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            file.createNewFile();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        UserCacheClass userCacheClass = makeEmptyData();
//        writeData(userCacheClass, file);
//        UserCacheJsonManager.saveFileStatic(userCacheClass);
//    }
//
//    // make empty data file
//    public static UserCacheClass makeEmptyData() {
//        return new UserCacheClass();
//    }
//
//    // write data to a file
//    public static void writeData(UserCacheClass settingsData, File file) {
//        Writer writer = null;
//        Gson gson = new Gson();
//
//        try {
//            writer = new FileWriter(file, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (writer == null) {
//            System.out.println("ERROR! Attempted writing to file \"" + file.getName() + "\" | Writer == null");
//            return;
//        }
//
//        //IF WRITER IS NOT NULL
//        gson.toJson(settingsData, writer);
//        try {
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            // TODO:
//        }
//    }
//
//    //loads player json data file
//    public UserCacheClass loadData() {
//        File file = getFile();
//        Gson gson = new Gson();
//        Reader reader = null;
//
//        try {
//            reader = new FileReader(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        if (reader == null) {
//            return null;
//        }
//
//        UserCacheClass ucc = gson.fromJson(reader, UserCacheClass.class);
//
//        assert ucc != null;
//        return ucc;
//    }
//
//    public void saveFile(UserCacheClass data) {
//        writeData(data, getFile());
//    }
//
//    public static void saveFileStatic(UserCacheClass data) {
//        writeData(data, getFile());
//    }
//
//    public static void addUser(String name) {
//        UserCacheClass cache = Minecraft.getMinecraft().getCapeUserCache().loadData();
//        cache.addUser(name);
//        saveFileStatic(cache);
//    }
//
//    public static void removeUser(String name) {
//        UserCacheClass cache = Minecraft.getMinecraft().getCapeUserCache().loadData();
//        cache.removeUser(name);
//        saveFileStatic(cache);
//    }
}

