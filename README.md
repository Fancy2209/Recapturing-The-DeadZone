# Recapturing The DeadZone

TLS:DZ private server (revival).

## How to Play?

1. Install [Java 24](https://www.oracle.com/java/technologies/downloads/). We recommend installing it in the default directory. (e.g., for Windows 64-bit users, download the x64 MSI installer).
2. Install [MongoDB community edition](https://www.mongodb.com/try/download/community). (You don't have to install MongoDB compass and installing it as a service is optional)
3. Download the server [latest release](https://github.com/glennhenry/Recapturing-The-DeadZone/releases) (download the `deploy.zip`).
4. Extract the zip.
5. Double click on `autorun.bat` to start the server (behind the scene, this script locate your MongoDB and Java default installation to start them both).
6. Open a flash-compatible browser (emulator like Ruffle is not supported), such as [Basilisk](https://www.mediafire.com/file/tmecqq7ke0uhqm7/Basilisk_with_Flash_%2528debug%2529.zip/file). Then, go to `127.0.0.1:8080`.

Join our [Discord](https://discord.gg/jFyAePxDBJ) for questions and more information.

## Development

### Requirements:

- Java 24
- MongoDB community edition

### Running the Server

1. You must run the MongoDB server first. Either do it manually in command line (or via scripts: `runmongo.bat` and `runmongo.sh`).
2. The server can be run in multiple ways:
   - On Intellij IDE, by clicking run the `ApplicationKt` main function.
   - Running the script `runserver.bat` or `runserver.sh`.
   - Or in command line by:

    ```bash
    .\gradlew run
    ```

3. The server will run on:
  - `127.0.0.1:8080` (Web server + API server)
  - `127.0.0.1:7777` (Socket server)
  
- Open the game via a web browser **with native Flash support** (Ruffle is not supported yet). **You must go to `127.0.0.1:8080`**.
