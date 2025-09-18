package com.makaty.code.Run;

import com.makaty.code.clientCLI.ClientCLI;

import java.io.IOException;

public class Client_CLI {

    public static void main(String[] args) throws IOException {
        ClientCLI cli = new ClientCLI();
        cli.fire();
    }
}
