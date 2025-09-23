package com.makaty.code.Server.Models;

import com.makaty.code.Common.Models.Status;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class ClientProfile {

    private String userName;
    private final UserConnection userConnection;
    private File currentDir;
    private File root;

    public ClientProfile() {

        root = new File(System.getProperty("user.dir")).getAbsoluteFile();
        currentDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        userConnection = new UserConnection();
    }

    // getters
    public SocketChannel getCommandSocketChannel() {return this.userConnection.getCommandSocketChannel();}
    public SocketChannel getDataSocketChannel() {return this.userConnection.getDataSocketChannel();}
    public String getRelativeWorkingDir() {
        File target = currentDir;

        Path basePath = root.toPath();
        Path targetPath = target.toPath();

        Path relativePath = basePath.relativize(targetPath);

        String res = relativePath.toString();
        return "\\".concat(res);
    }
    public Object getAbsoluteWorkingDir() {return currentDir.getAbsolutePath();}
    public String getUserName() {return userName;}

    // setters
    public void setUserName(String newUserName){this.userName = newUserName;}
    public void setDataSocketChannel(SocketChannel dataSocketChannel) {
        userConnection.setDataSocketChannel(dataSocketChannel);
    }
    public void setCommandSocket(SocketChannel commandSocketChannel) {
        userConnection.setCommandSocketChannel(commandSocketChannel);
    }


    public void closeConnection() throws IOException {userConnection.close();}

    public Status changeWorkingDir(String directoryName) {
        try{
            File newDir = new File(currentDir, directoryName).getCanonicalFile();
            if(!newDir.exists()) return Status.NO_FILE_EXISTS;
            if(!newDir.isDirectory()) return Status.FILE_NOT_DIR;

            currentDir = newDir.getAbsoluteFile();
            return Status.SUCCESS;
        } catch (IOException e) {
            return Status.NO_FILE_EXISTS;
        }


    }
}
