package com.makaty.code.Client.Models;

import java.util.Random;

public class ClientConfig {
    public static final boolean LAN = false;
    public static int PORT = 2025;
    public static String USER_NAME = "TestUser" + new Random().nextInt(100);
    public static String HOST_NAME = LAN ? "0.0.0.0" : "127.1.1.1";
}
