package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

        LoggerManager.getInstance().info(
                String.format("[RETRIEVAL_START] file=%s size=%.2f%s", fileName, roundedSize, unit)
        );

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
            }

        }

        LoggerManager.getInstance().info(String.format("File '%s' received successfully.", fileName));
    }

    public void sendFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) {
            LoggerManager.getInstance().error("Specified file doesn't exist!");
            return;
        }

        String fileName = file.getName();
        long size = file.length();
        String unit = resolveProperUnit(size);
        double roundedSize = resolveProperSize(size, unit);


        LoggerManager.getInstance().info(
                String.format("[UPLOAD_START] file=%s size=%.2f%s", fileName, roundedSize, unit)
        );
        SocketChannel dataSocket = ConnectionManager.getInstance().getDataSocketChannel();
        long uploadedBytes = 0L;
        long bytesRead;


        try(FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(CHUNK);

            while((bytesRead = fileChannel.read(buffer)) != -1) {
                if(bytesRead == 0) continue;

                // write data on socket
                buffer.flip();
                while(buffer.hasRemaining()) {
                    dataSocket.write(buffer);
                }

                uploadedBytes += bytesRead;

                double progress = (100.0 * uploadedBytes) / size;
                LoggerManager.getInstance().fileProgress(progress);
                buffer.clear(); // clear the buffer for next iteration read.
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LoggerManager.getInstance().info(String.format("File '%s' uploaded successfully.", fileName));
    }



}
