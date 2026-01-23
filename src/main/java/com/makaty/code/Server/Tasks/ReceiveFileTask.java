package com.makaty.code.Server.Tasks;

import com.makaty.code.Server.Models.ClientProfile;
import com.makaty.code.Server.Models.TaskDispatcher;
import com.makaty.code.Server.Server;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;

public class ReceiveFileTask extends DataTask {


    private static final int KB = 1024;
    private static final int CHUNK_SIZE = 64 * KB;
    private final File targetFile;
    private final long fileSize;
    private final long uploadedSize;

    public ReceiveFileTask(ClientProfile clientProfile, File file, long fileSize, long uploadedSize) {
        super(clientProfile);
        this.targetFile = file;
        this.fileSize = fileSize;
        this.uploadedSize = uploadedSize;

    }

    @Override
    public Void call() throws Exception {
        SocketChannel clientDataChannel = clientProfile.getDataSocketChannel();

        try (
            FileChannel fileChannel =
                FileChannel.open(
                        targetFile.toPath(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND
                )
        ) {
            // if file already uploaded
            if(uploadedSize >= fileSize) return null;

            ByteBuffer buffer = ByteBuffer.allocate(CHUNK_SIZE);

            int totalRead = 0;
            while (buffer.hasRemaining() && (totalRead + uploadedSize) < fileSize) {
                int bytesRead = clientDataChannel.read(buffer);

                if (bytesRead == -1) {
                    // client finished upload
                    break;
                }

                if (bytesRead == 0) {
                    // socket empty for now
                    break;
                }

                totalRead += bytesRead;
            }


            buffer.flip();
            fileChannel.write(buffer);

            long newUploadedSize = uploadedSize + totalRead;
            if (newUploadedSize < fileSize) {
                // receive the rest of the file
                TaskDispatcher.getInstance().submitAsyncTask(new ReceiveFileTask(clientProfile, targetFile, fileSize, newUploadedSize));
            } else {
                Server.serverLogger.info(String.format("File '%s' received successfully.", targetFile.getName()));
            }





        } catch (IOException e) {
            Server.serverLogger.warn(String.format("Client \"%s\" Disconnected, file upload canceled.", clientProfile.getUserName()));
        }
        return null;
    }
}
