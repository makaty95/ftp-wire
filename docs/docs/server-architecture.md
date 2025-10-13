The **Server** Module is responsible for receiving and executing commands sent by clients.
It identifies the appropriate handler for each command, builds the corresponding tasks, and schedules them for execution on worker threads.
After processing, the server may reply to the client or send files stored on disk.

![Client Module Architecture](diagrams/Server.webp)

**Lets Explain the above diagram in depth.**

### CommandSelectorDispatcher 
> This module is responsible for receiving different clients commands, unpack it to a `Command` and then proceeed to recognize the command type following it's `CommandHandler`.
Then it will pass the client command to the handler for the next step.

### CommandHandler 
> Each command type have a specified `CommandHandler` which is responsible for handling client commands of that type.<br>
The handler takes the command then it does all the dirty work like checking for command validity or parameters, check if the user have a registerd session on the server `SessionRegistry`, etc..
Then, it uses `TaskDispatcher` to do the acctual work.

### TaskDispatcher
> Each user command is transformed into a set of tasks which can be done either **Synchronously** or **Asynchronously**. The module will take a **Task** and then schedules this task on it's ThreadPool.

### ThreadPool
> The <a href="https://en.wikipedia.org/wiki/Thread_pool" target="_blank">Thread Pool Design</a> is a common software design pattern which is used to perform many tasks at the same time with equal **CPU utilization** for all tasks. This is importatnt because we don't want some clients to have a better performance at the expense of some other clients which would results bad usability.


