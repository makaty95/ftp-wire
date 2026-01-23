package com.makaty.code.Server.Tasks;


import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.ClientProfile;
import com.makaty.code.Server.Models.TaskDispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;

// the instances of this object will be submitted to a thread pool
public class SendFileTask extends DataTask {

    private static final int KB = 1024;
    private static final int CHUNK_SIZE = 64 * KB;
    private final File targetFile;
    private final long offset;
    public SendFileTask(ClientProfile clientProfile, File file, long offset) {
        super(clientProfile);
        this.targetFile = file;
        this.offset = offset;
    }

    @Override
    public Void call() {
        //System.out.println("Send file called. offset = " + offset);

        try (FileChannel fileChannel = FileChannel.open(targetFile.toPath(), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(CHUNK_SIZE);

            int bytesRead = fileChannel.read(buffer, offset);
            if (bytesRead == -1) {
                // EOF reached
                Server.serverLogger.info(String.format("File '%s' sent successfully.", targetFile.getName()));
                return null;
            }
            buffer.flip();

            // Write chunk to client's data socket
            SocketChannel clientDataChannel = clientProfile.getDataSocketChannel();

            if (!clientDataChannel.isOpen() || !clientDataChannel.isConnected()) {
                throw new IOException();
            }

            while (buffer.hasRemaining()) {
                clientDataChannel.write(buffer);
            }

            // Update position
            long newOffset = offset + bytesRead;

            // send the rest of the file
            TaskDispatcher.getInstance().submitAsyncTask(new SendFileTask(clientProfile, targetFile, newOffset));

        } catch (IOException e) {
            Server.serverLogger.warn(String.format("Client \"%s\" Disconnected, file retrieval canceled.", clientProfile.getUserName()));
        }

        return null;

    }
}
