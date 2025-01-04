# FTP-Server
This project is a simple FTP server implemented in Java using Socket programming, designed to handle client connections and facilitate file transfers.
- Features

  - Multi-client support: Handles multiple client connections simultaneously.
  - File transfer: Enables clients to upload and download files.
  - Command execution: Processes client commands for various operations.
- Prerequisites
  - Java Development Kit (JDK): Ensure JDK 8 or higher is installed.
 
  ### Project Structure
    ``` plaintext
    Copy code
    FTP-Server/
    └── src/
        ├── Client/
        │   ├── Client.java
        │   ├── ClientUI.java
        │   ├── DataReceiver.java
        │   └── ReplyReceiver.java
        ├── Server/
        │   ├── Server.java
        │   ├── ServerServiceThread.java
        │   └── CommandServiceThread.java
        └── Common/
            ├── Command.java
            |── CommandInfo.java
            ├── Reply.java
            ├── Status.java
            └── Utility.java
    ```
- Server: Contains server-side classes managing client connections and command execution.
- Common: Includes shared utilities and data structures used by both server and client components.

