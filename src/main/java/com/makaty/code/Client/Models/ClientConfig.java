package com.makaty.code.Client.Models;

import java.util.Random;

public class ClientConfig {
    public static int LOCAL_PORT = 3030;
    public static int REMOTE_PORT = 2121;
    public static String LOCAL_HOST = "localhost";
    public static String REMOTE_HOST = "localhost";
    public static String USER_NAME = "TestUser" + new Random().nextInt(100);


}
