<p align="center">
  <img src='https://img.shields.io/badge/Java-8%2B-blue?logo=java&logoColor=white' />
  <img src='https://img.shields.io/badge/build-passing-brightgreen' />
  <img src='https://img.shields.io/badge/license-MIT-yellow' />
  <img src='https://img.shields.io/github/issues/makaty95/ftp-server' /> <br>
  <img src='https://img.shields.io/github/stars/makaty95/ftp-server?style=social' />
  <img src='https://img.shields.io/github/forks/makaty95/ftp-server?style=social' />


</p>

<p align="center">
  <img width="700" height="300" alt="Image" src="https://github.com/user-attachments/assets/c3fb5f79-b2fd-4501-b7ba-3f31ce5b036d" />
</p>

<h4 align='center'> A simple extensible FTP server implemented in Java. </h4>



---

## 📌 Prerequisites
- ☕ **Java Development Kit (JDK)**: Version **8 or higher**

---

## ✨ Features
-  **Multi-client support** – handles multiple connections simultaneously.
-  **Scalable & High-performance** – uses **Thread Pool architecture** and **Non-blocking I/O (NIO)**.
-  **File transfer** – clients can download files from the server.
- **Command execution** – processes various client commands.
-  **Logging & Monitoring system** – structured logs via `ClientLogger` / `ServerLogger` or debugging, tracking client activity, and monitoring transfers.
-  **Error handling & Disconnection discovery** – Clean detection of remote disconnections and graceful shutdown/termination logic of client disconnections.
-  **Extensible design patterns** – leverages reusable patterns for future extensions.

---

## 📜 Supported Commands

| Command | Parameters               | Description                                                                      | Status |
|:--------|:-------------------------|:---------------------------------------------------------------------------------|:------:|
| `HELP`  | `No parameters`          | Lists all supported commands.                                                    | ✅ |
| `QUIT`  | `No parameters`          | Terminates the client connection.                                                | ✅ |
| `RETR`  | `file_path` `file_name`  | Retrieves a file from the server. Optionally rename it by providing `file_name`. | ✅ |
| `PWD`   | `No parameters`          | Displays the current working directory.                                          | ✅ |
| `CWD`   | `directory_path`         | Changes the current working directory.                                           | ✅ |
| `NLIST` | `directory_path`         | List all files and folders inside a directory.                                   | ✅ |
| `STOR`  | `file_path` `file_name`  | Uploads a file to the server. Optionally rename it by providing `file_name`.     | ⬜ |

✅ = Implemented & tested  
⬜ = Planned

---

## 📂 Project Structure
```plaintext
├── Client
│   │
│   ├── Controllers
│   │   ├── CommandController.java
│   │   └── DataController.java
│   │
│   ├── Loggers
│   │   └── ClientLogger.java
│   │
│   ├── ReplyHandlers
│   │   ├── FILE_INFO_ReplyHandler.java
│   │   ├── MESSAGE_ReplyHandler.java
│   │   └── ReplyHandler.java  
│   │ 
│   │        
│   ├── Models
│   │   ├── ClientConfig.java
│   │   ├── CommandSender.java
│   │   ├── ConnectionManager.java
│   │   ├── LoggerManager.java
│   │   ├── Reply.java
│   │   ├── ReplyPacketFactory.java
│   │   └── ResponseReceiver.java
│   │
│   └── Client.java
│
├── clientCLI
│   ├── ClientCLI.java
│   └── ClientCLILogger.java
│
├── Common
│   │
│   ├── Exceptions
│   │   └── RemoteDisconnectionException.java
│   │
│   ├── Loggers
│   │   └── Logger.java
│   │
│   ├── Models
│   │   ├── Command.java
│   │   └── Status.java
│   │
│   ├── Packets
│   │   ├── Communication
│   │   │   ├── CommandPacket.java
│   │   │   └── ReplyPacket.java
│   │   │
│   │   ├── HansShaking
│   │   │   ├── DonePacket.java
│   │   │   ├── HelloPacket.java
│   │   │   ├── PairPacket.java
│   │   │   └── WelcomePacket.java
│   │   │
│   │   └── IO
│   │       ├── PacketReader.java
│   │       ├── PacketWriter.java
│   │       └── Packet.java
│   │   
│   │   
│   │── Serialization
│   │   └── PacketSerializer.java
│   │
│   │
│   └── Types
│       ├── PacketType.java
│       └── ReplyType.java
│    
├── Server
│   ├── Exceptions
│   │   ├── CanNotReadPacketException.java
│   │   └── NoCommandWithSpecifiedHeaderException.java
│   │
│   ├── Handlers
│   │   ├── CommandErrorHandler.java
│   │   ├── CommandHandler.java
│   │   ├── ErrorHandler.java
│   │   ├── HelpCommandHandler.java
│   │   ├── QuitCommandHandler.java
│   │   └── RetrieveFileCommandHandler.java
│   │
│   ├── HandShaking
│   │   ├── HandShakeManager.java
│   │   └── Session.java
│   │
│   ├── Loggers
│   │   ├── ServerCLILogger.java
│   │   └── ServerLogger.java
│   │
│   ├── Models
│   │   ├── Types
│   │   │   ├── CommandType.java
│   │   │   └── ErrorType.java
│   │   │
│   │   ├── ClientProfile.java
│   │   ├── UserConnection.java
│   │   ├── TaskDispatcher.java
│   │   ├── ServerConfig.java
│   │   ├── CommandSelectorDispatcher.java
│   │   └── ThreadPool.java
│   │
│   │
│   ├── Registeries
│   │   ├── CommandRegistry.java
│   │   └── SessionRegistry.java
│   │
│   │
│   ├── SocketAcceptors
│   │   ├── CommandSocketAcceptor.java
│   │   └── DataSocketAcceptor.java
│   │
│   ├── Tasks
│   │   ├── CommandTask.java
│   │   ├── DataTask.java
│   │   ├── SendFileTask.java
│   │   ├── SendPacketTask.java
│   │   └── Task.java
│   │
│   │
│   ├── Regist
│   │   ├── CommandTask.java
│   │   ├── DataTask.java
│   │   ├── SendFileTask.java
│   │   ├── SendPacketTask.java
│   │   └── Task.java
│   │
│   └── Server.java
│
│
└── Run
    ├── Client_CLI.java
    └── Server_CLI.java

```
---
## Resources used
- https://www.rfc-editor.org/rfc/rfc959
- https://www.hostitsmart.com/manage/knowledgebase/392/What-is-the-Difference-Between-HTTP-and-FTP.html
- http://www.nsftools.com/tips/RawFTP.htm