<!-- Coming soon. :fontawesome-regular-face-grin: -->
## Client
Run the client on the terminal. it should display something like this
```bash
$ ./ftp-wire-client.sh
Terminal type: xterm-256color

   ___  __
 /'___\/\ \__                               __
/\ \__/\ \ ,_\  _____            __  __  __/\_\  _ __    __
\ \ ,__\\ \ \/ /\ '__`\  _______/\ \/\ \/\ \/\ \/\`'__\/'__`\
 \ \ \_/ \ \ \_\ \ \L\ \/\______\ \ \_/ \_/ \ \ \ \ \//\  __/
  \ \_\   \ \__\\ \ ,__/\/______/\ \___x___/'\ \_\ \_\\ \____\
   \/_/    \/__/ \ \ \/           \/__//__/   \/_/\/_/ \/____/
                  \ \_\
                   \/_/

==================================================
                  (OFFLINE MODE)
==================================================
(TestUser18)>
```
The client is initially in offline mode which allows you to specify many configurations such as your IP, username, and so on.
You can show all available offline to use commands by typing `help`
```bash
(TestUser18)> help

----------------------------------------------------------------------------------------------------
| Command              | Parameters                     | Description                              |
----------------------------------------------------------------------------------------------------
| [1]set_username      | new_username                   | set your username which will be sent to  |
|                      |                                | the remote.                              |
| [2]set_remote        | remote_hostname remote_port    | set the hostname and port of the remote. |
| [3]set_local         | local_hostname local_port      | set the hostname and port of your        |
|                      |                                | machine.                                 |
| [4]connect           |                                | initialize the connection between your   |
|                      |                                | machine and the remote.                  |
| [5]clear             |                                | clears your terminal screen.             |
| [6]conn_info         |                                | display info about both local and remote |
|                      |                                | connection.                              |
| [7]help              |                                | display commands info list.              |
----------------------------------------------------------------------------------------------------
(TestUser18)>
```
This will show you all the commands which you can use to configure your client before your connection with remote.<br>
Lets walkthrough those one by one:

- `set_username`
> This simply sets your *username* locally and also when connecting to the server.
```bash
(TestUser18)> set_username Terminator00
(Terminator00)>
```
- `set_remote`
> This sets the server *hostname* and *port* which you will be connecting to.
```bash
(Terminator00)> set_remote 192.168.1.13 2121
(Terminator00)>
```
- `set_local`
> This sets your *hostname* and *port* which will be used to identify your client on the remote.
```bash
(Terminator00)> set_local 192.168.1.8 8989
(Terminator00)>

```
- `conn_info`
> Now you can display all your configurations using this command.
```bash
(Terminator00)> conn_info
Remote:
         IP: 192.168.1.13
         PORT: 2121
Local:
         IP: 192.168.1.8
         PORT: 8989
         Username: Terminator00
(Terminator00)>

```
- `clear`
> This will just clear your terminal screen, nothing fancy.

- `connect`
> This will instantiate the ftp connection between your client and the server. You are now online and can easily use all 
the FTP commands such as `RETR`, `PWD`, `CWD`, etc..<br>
You can also recognize the path preceeded with `@` which is shown near your username: `@/`, this shows your current opened working directory on the server.<br>
Check the complete commands list [here](index.md#supported-commands)
```bash
(Terminator00)> connect
==================================================
                  (ONLINE MODE)
==================================================
(Terminator00@\)>
```
> !!! wire-note "Wrong remote info"
     If you entered a wrong ftp-wire server address or port, it will result in a connection failure.
     ```bash
     (Terminator00)> connect
     [ERROR]: Failed to connect to the remote, please verify remote info.
     ==================================================
                     (OFFLINE MODE)
     ==================================================
     (Terminator00)>
     ```

## Server
Run the server on your terminal
```bash
$ ./ftp-wire-server.sh
[SERVER_LOGGER] [INFO]: CommandAcceptor instance is up and running.
[SERVER_LOGGER] [INFO]: DataAcceptor instance is up and running.
[SERVER_LOGGER] [INFO]: Server started successfully.
[SERVER_LOGGER] [INFO]: Command Selector Dispatcher is up and running.
```
Now your server is running and listening for clients to connect, Simple as that.
!!! wire-note "Server Module"
    The server module is under developement. It needs more control which allows you to specify many attributes such as
    IP or PORT. Also, registering clients accounts (for future login mechanism) so that a client will be allowed to access the content only if it is registered.
    All those functionalities should be implemented in the near future Inshallah.
