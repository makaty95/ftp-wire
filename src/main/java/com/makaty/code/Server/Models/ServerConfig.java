package com.makaty.code.Server.Models;


public class ServerConfig {
    public static final boolean LAN = false; // false for testing, true for real world
    public static final int COMMAND_PORT = 2121;
    public static final int DATA_PORT = 2020;
    public static final String COMMAND_HOST = LAN ? "0.0.0.0" : "localhost" ;
    public static final String DATA_HOST = LAN ? "0.0.0.0" : "localhost" ;
    public static final int WORKER_THREADS = 5;


}

