## Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **An IDE of your choose (Eclipse, VSCode, Intellij, etc..)**
---

## Installation
You can use any command line too such as gitbash, cmder. powershell or even integrated terminals like IntelliJ terminal
for the installation step.
```bash
git clone https://github.com/makaty95/ftp-wire.git
```
Enter the project directory
```bash
cd ftp-wire
```

## Build
Build the project using maven
```bash
mvn clean install
```
This should generate a directory called `target`, which will have the runnable jar files for both client and server.

## Run
To run the client or the server you can simply use the following commands of java
```bash
java -jar target/client-cli.jar
```
```bash
java -jar target/server-cli.jar
```
Or you can use the shell files

```bash
./ftp-wire-client.sh
```
```bash
./ftp-wire-server.sh
```
