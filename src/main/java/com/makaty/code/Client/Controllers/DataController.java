package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DataController {

    private static final int CHUNK = 128;
    private static DataController instance;

    /// Helper functions
    private DataController() {}

    public static DataController getInstance() {
        if(instance == null) {
            instance = new DataController();
        }
        return instance;
    }

    private String constructURI(String fileName, String fileLocation) {

        if(fileLocation.endsWith("/") && !fileName.startsWith("/")) return fileLocation.concat(fileName);

        if(!fileLocation.endsWith("/") && fileName.startsWith("/")) return fileLocation.concat(fileName);

        if(fileLocation.endsWith("/") && fileName.startsWith("/")) {
            return fileLocation.concat(fileName.substring(1, fileName.length()-1));
        }

        return fileLocation.concat("/").concat(fileName);


    }

    private String resolveProperUnit(Long size) {
        if(size < (1024)) return "B";
        if(size < (1024 * 1024)) return "KB";
        if(size < (1024 * 1024 * 1024)) return "MB";
        return "GB";
    }

    private double resolveProperSize(Long size, String type) {
        return switch (type) {
            case "B" -> size;
            case "KB" -> ((double) size) / (1024.0);
            case "MB" -> ((double) size) / (1024.0 * 1024.0);
            default -> ((double) size) / (1024.0 * 1024.0 * 1024.0);
        };

    }

    private void skipFileBytes(long size) throws IOException {
        SocketChannel dataSocket = ConnectionManager.getInstance().getDataSocketChannel();
        ByteBuffer buffer = ByteBuffer.allocate(CHUNK);
        long skippedBytes = 0L;

        while (skippedBytes < size) {
            buffer.clear();
            int bytesRead = dataSocket.read(buffer);

            if (bytesRead == -1) {
                LoggerManager.getInstance().warn("Connection closed before file discard completed.\n");
                break;
            } else if (bytesRead == 0) {
                continue;
            }

            skippedBytes += bytesRead; // just count, don't write anywhere
        }

        LoggerManager.getInstance().info("File skipped.\n");

    }


    public void receiveFile(String fileName, String location, long size) throws IOException {


        String URI = constructURI(fileName, location);
        String unit = resolveProperUnit(size);
        double roundedSize = resolveProperSize(size, unit);


        // Validate if all path folders exist
        // if not it will automatically be created.
        if(!validateAllDirectories(URI)) {
            LoggerManager.getInstance().warn("Can not receive file, file path is invalid.\n");
            skipFileBytes(size);
            return;
        }

        LoggerManager.getInstance().info(String.format("Receiving ['%s' | size = %.2f%s]...\n", fileName, roundedSize, unit));
        SocketChannel dataSocket = ConnectionManager.getInstance().getDataSocketChannel();
        long loadedBytes = 0L;



        try(FileOutputStream fos = new FileOutputStream(URI)) {
            ByteBuffer buffer = ByteBuffer.allocate(CHUNK);

            while (loadedBytes < size) {
                buffer.clear();  // reset buffer before reading
                int bytesRead = dataSocket.read(buffer);

                if (bytesRead == -1) {
                    LoggerManager.getInstance().warn("Connection terminated before file transfer completed.\n");
                    break;
                } else if (bytesRead == 0) {
                    continue;
                }

                fos.write(buffer.array(), 0, bytesRead);
                loadedBytes += bytesRead;

                // Optional: progress logging
                double progress = (100.0 * loadedBytes) / size;
                LoggerManager.getInstance().info("@progress " + String.format("Progress: %.2f%%", progress));
            }
        }

        LoggerManager.getInstance().info("@progress:end");
        LoggerManager.getInstance().info(String.format("File ['%s'] received.", fileName));
    }

    private boolean validateAllDirectories(String uri) {
        File file = new File(uri);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            return parentDir.mkdirs(); // create all parent directories if needed
        }
        return true;
    }


    public void sendFile(String filePath) {
        //TODO: implement this
    }



}
