<p align="center">
  <img src='https://img.shields.io/badge/Java-8%2B-blue?logo=java&logoColor=white' />
  <img src='https://img.shields.io/badge/build-passing-brightgreen' />
  <img src='https://img.shields.io/badge/license-MIT-yellow' />
  <img src='https://img.shields.io/github/issues/makaty95/ftp-server' /> <br>
  <img src='https://img.shields.io/github/stars/makaty95/ftp-server?style=social' />
  <img src='https://img.shields.io/github/forks/makaty95/ftp-server?style=social' />

</p>

![ftp-wire-logo](images/ftp-wire-logo.png)

A **multi-client FTP system** built in Java, supporting **file transfers, authentication, and command-based communication** between clients and a central server.

---

## Overview
**FTP-WIRE** was built to be an extensible, effecient and most importantly scalable solution for the file transfering service on a computer machine. it is mainly built of 2 modules: a `Client` and a `Server` modules, these modules should be integratable to any other apps in the future which need to use an ftp service (may be not yet acquired, but this is a long term goal).

Many aspects where taken into consideration while designing such software architecture and functions including but not limited to:

- Effecient CPU utilization in the server module.
- Usage of many Design Patterns which results a generic code and improve scalability in the future, such as adding new commands or changing a
  packet contents.

The current project version had been refactored about 2 times before reaching this stage of the developement.

## License
The project uses the <a href="https://en.wikipedia.org/wiki/MIT_License" target="_blank"> MIT license </a>.


## Features
- **Authentication** & **Handshaking** system
- Command-based protocol (`LIST`, `RETR`, `STOR`, etc.)
- Modular structure with `Common`, `Server`, and `Client` packages
- **Multi-client support** – handles multiple connections simultaneously.
- **Scalable & High-performance** – uses **Thread Pool architecture** and **Non-blocking I/O (NIO)**.
- **File transfer** – clients can download files from the server.
- **Command execution** – processes various client commands.
- **Logging & Monitoring system** – structured logs via `ClientLogger` / `ServerLogger` or debugging, tracking client activity, and monitoring transfers.
- **Error handling & Disconnection discovery** – Clean detection of remote disconnections and graceful shutdown/termination logic of client disconnections.
- **Extensible design patterns** – leverages reusable patterns for future extensions.

---

## Supported Commands

| Command | Parameters              | Description                                                                      |                  Status                   |
| :------ | :---------------------- | :------------------------------------------------------------------------------- | :---------------------------------------: |
| `HELP`  | `No parameters`         | Lists all supported commands.                                                    | :material-checkbox-marked-circle-outline: |
| `QUIT`  | `No parameters`         | Terminates the client connection.                                                | :material-checkbox-marked-circle-outline: |
| `RETR`  | `file_path` `file_name` | Retrieves a file from the server. Optionally rename it by providing `file_name`. | :material-checkbox-marked-circle-outline: |
| `PWD`   | `No parameters`         | Displays the current working directory.                                          | :material-checkbox-marked-circle-outline: |
| `CWD`   | `directory_path`        | Changes the current working directory.                                           | :material-checkbox-marked-circle-outline: |
| `NLIST` | `directory_path`        | List all files and folders inside a directory.                                   | :material-checkbox-marked-circle-outline: |
| `STOR`  | `file_path` `file_name` | Uploads a file to the server. Optionally rename it by providing `file_name`.     | :material-checkbox-marked-circle-outline: |

!!! wire-note "Status indication"

    :material-checkbox-marked-circle-outline: :material-arrow-right-bold: Implemented & tested

    :material-progress-clock: :material-arrow-right-bold: Planned
---


## Project Core Modules

| Package  | Description                                                 |
| -------- | ----------------------------------------------------------- |
| `Server` | Core server logic, client management, and file handling     |
| `Client` | An FTP client which is used to connect and transfer files   |

See the full Project Structure [here](architecture-overview/#project-structure)

---

## Contributing

Everyone is welcomed to contribute to **FTP-WIRE** project. Contribution can be techinical such as recommending or implementing a new feature, bugs fixing/discovery, or non-technical such as docs writing and fixing.

Kindly find the full contribution guide [here](contributing.md)

## Quick Links

- [Getting Started](getting-started.md)
- [Architecture Overview](architecture-overview.md)
- [Usage Guide](usage.md)
