package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void receiveFile(String fileName, String location, long size) throws IOException {

        Path filePath = Paths.get(location, fileName);
        Files.createDirectories(filePath.getParent());

        String unit = resolveProperUnit(size);
        double roundedSize = resolveProperSize(size, unit);


        LoggerManager.getInstance().info(String.format("Receiving ['%s' | size = %.2f%s]...\n", fileName, roundedSize, unit));
        SocketChannel dataSocket = ConnectionManager.getInstance().getDataSocketChannel();
        long loadedBytes = 0L;




        try(FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
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
                LoggerManager.getInstance().fileProgress(progress);
                //LoggerManager.getInstance().info(String.format("Progress: %.2f%%", progress));
            }

        }


        LoggerManager.getInstance().info(String.format("File ['%s'] received.\n", fileName));
    }

    public void sendFile(String filePath) {
        //TODO: implement this
    }



}
