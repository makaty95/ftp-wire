# FTP-Server
This project is a simple FTP server implemented in Java using Socket programming, designed to handle client connections and facilitate file transfers.

### Prerequisites
  - Java Development Kit (JDK): Ensure JDK 8 or higher is installed.
 
### Features

- Multi-client support: Handles multiple client connections simultaneously.
- Scalability: hadles high load of clients requests using Thread Pool Architecture
- Support of NonBlocking IO: increases Server performance and utilizes the CPU processing 
- File transfer: Enables clients to download files from another machine.
- Command execution: Processes client commands for various operations.
- Commands:

    | Command     | Parameters                       | Description                                                                                                                                                                 |         Status         |
    |:------------|:---------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------:|
    | `cmds`      | `No parameters`                  | Gets all the commands which could be sent to the server                                                                                                                     |   :white_check_mark:   |
    | `bye`       | `No parameters`                  | Terminates the connection with the server                                                                                                                                   |   :white_check_mark:   |
    | `get_file`  | `file_path`  `file_name`         | Gets a file specified with the path 'file_path'.<br/> It is optional to provide the second parameter which will change the filename to 'file_name'                          |   :white_check_mark:   |
    | `push_file` | `file_path`  `file_name`         | send a file with the path 'file_path' to the server.<br/> It is optional to provide the second parameter which will change the filename to 'file_name'                      | :black_square_button:  |
    | `ls`        | `No parameters`                  | Displays the current path files and folders                                                                                                                                 | :black_square_button:  |
    | `cd`        | `file_path`                      | changes the current access path                                                                                                                                             | :black_square_button:  |
    
### Bugs
:ballot_box_with_check: Client connection disconnected accidentally when getting 2 files

:ballot_box_with_check: Client-side infinite loop when after receiving a file done

:ballot_box_with_check: file receiving percentage overflow (38893%, 5889%, ...)

:ballot_box_with_check: Server client sockets isn't closing when client exits

:black_square_button: When 2 Clients order the same file at the same time using different threads, the server serves them sequentially instead of parallel

:black_square_button: When Client exits without $bye$ command, the server have no idea




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
    |   |── Functionalities/
    |   |   └── Core/
    |   |       |── SendCMDS.java
    |   |       |── SendFile.java
    |   |       └── SendReply.java
    |   | 
    │   ├── Server.java
    │   ├── ServerService.java
    │   ├── Task.java
    │   └── ThreadPool.java
    └── Common/
        ├── Command.java
        |── CommandInfo.java
        ├── Reply.java
        ├── Status.java
        └── Utility.java
```
- Server: Contains server-side classes managing client connections and command execution.
- Client: Contains client-side classes which can connect to the server, send commands and receive replies and data
- Common: Includes shared utilities and data structures used by both server and client components.
- ## Resources used
  - https://www.rfc-editor.org/rfc/rfc959
  - https://www.hostitsmart.com/manage/knowledgebase/392/What-is-the-Difference-Between-HTTP-and-FTP.html
  - http://www.nsftools.com/tips/RawFTP.htm
  

- ## System Diagrams
  ![Functions](https://drive.google.com/file/d/1MVRgoa5Gu2QSecNyey3JaXACOxWJc4pF)
  ![PipeLine](https://drive.google.com/file/d/1ZvQPat1uQB_LYnOShvE8MNTIVnmFv0La)
