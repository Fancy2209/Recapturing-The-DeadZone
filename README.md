# Recapturing The DeadZone

TLS:DZ private server (revival).

## How to Play?

1. Install [Java 24](https://www.oracle.com/java/technologies/downloads/). We recommend installing it in the default directory. (e.g., for Windows 64-bit users, download the x64 MSI installer).
2. Install [MongoDB community edition](https://www.mongodb.com/try/download/community). (You don't have to install MongoDB compass and installing it as a service is optional)
3. Download the server [latest release](https://github.com/glennhenry/Recapturing-The-DeadZone/releases) (download the `deploy.zip`).
4. Extract the zip.
5. Run the MongoDB server, this can be done by running the `runmongo.bat/sh` scripts (for more information see MongoDB's tutorial, [this is for Windows](https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-windows/)).
5. Run the game server, this can be done by running the `autorun.bat/sh` script (behind the scene, this script locate your Java default installation to run the server).
6. Open a flash-compatible browser (emulator like Ruffle is not supported), such as [Basilisk](https://www.mediafire.com/file/tmecqq7ke0uhqm7/Basilisk_with_Flash_%2528debug%2529.zip/file). Then, go to `127.0.0.1:8080`.

Join our [Discord](https://discord.gg/jFyAePxDBJ) for questions and more information.

## Development

### Requirements:

- Java 24
- MongoDB community edition

### Running the Server

1. Run MongoDB server (from scripts: `runmongo.bat/sh`).
2. Run game server:
   - from scripts: `runserver.bat/sh`.
   - via command line `.\gradlew run`.
   - via Intellij IDE by clicking run on `Application.Kt`.
