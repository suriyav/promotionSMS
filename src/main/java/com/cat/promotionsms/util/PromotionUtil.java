package com.cat.promotionsms.util;

import com.cat.promotionsms.model.FileInfoModel;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class PromotionUtil {
    private final static Logger log = Logger.getLogger(PromotionUtil.class);
    private static FileInfoModel fileNameLog = new FileInfoModel();

    public static TreeSet<String> ReadFile(String pathFinder) throws IOException {
        File folder = new File(pathFinder);
        File[] listOfFiles = folder.listFiles();
        TreeSet<String> tFiles = null;

        for (File file : listOfFiles) {
            tFiles = new TreeSet<>();
            fileNameLog.setFileNameLog(file.getName());
            log.info("File :  " + folder + file.getName());
            if (file.exists()) {
                String line = null;
                int lines = 0;
                ArrayList<String> result = new ArrayList<>();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                    //This will read the first line
                    reader.readLine();
                    lines++;
                    while ((line = reader.readLine()) != null) { //Loop will run from 2nd line
                        lines++;
                        tFiles.add(line);
                    }
                } catch (IOException e) {
                    log.error("Error reading file '" + file.getName() + "'");
                    e.printStackTrace();
                }
                log.info("Data Count : " + (lines - 1) + " rows.");
                log.info("File name: " + file.getName());
                log.info("Absolute path: " + file.getAbsolutePath());
                log.info("File size in bytes: " + file.length());
            } else {
                log.info("The file does not exist.");
                return tFiles;
            }
        }
        return tFiles;
    }

    public static void WriteFile(String path, ArrayList<String> dataFile) throws IOException {
        String pathWriteLog = null;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        pathWriteLog = path + fileNameLog.getFileNameLog();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathWriteLog));
            for (String data : dataFile) {
                writer.write(data + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            log.error("Not Write file : " + e.getMessage());
        }
    }

    public static void MoveFile(String pathFile, String destinatione) {
        File folder = new File(pathFile);
        File[] listOfFiles = folder.listFiles();
        Path sourcepath = null;
        Path destinationepath = null;

        String FILE_PATH = destinatione;
        String FILE_EXTENSION = "txt";
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = FILE_PATH + "Archive_POSTPAID_" + df.format(new Date()) + "." + FILE_EXTENSION;
        try {
            for (File files : listOfFiles) {
                sourcepath = Paths.get(files.getAbsolutePath());
                destinationepath = Paths.get(filename);
                Files.move(sourcepath, destinationepath, StandardCopyOption.REPLACE_EXISTING);
                log.info("Move file success");
                files.deleteOnExit();
                log.info("Delete file success");
            }
        } catch (Exception e) {
            log.error("Message" + e.getMessage());
        }
    }
}

