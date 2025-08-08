# Recapturing The DeadZone

TLS:DZ private server (revival).

## How to Play?

1. Install [Java 24](https://www.oracle.com/java/technologies/downloads/). We recommend installing it in the default directory. (e.g., for Windows 64-bit users, download the x64 MSI installer).
2. Download the server [latest release](https://github.com/glennhenry/Recapturing-The-DeadZone/releases) (download the `deploy.zip`).
3. Extract the zip.
4. Double click on `autorun.bat` to start the server (behind the scene, this script locate your Java default installation and start the server file).
5. Open a flash-compatible browser (emulator like Ruffle is not supported), such as [Basilisk](https://www.mediafire.com/file/tmecqq7ke0uhqm7/Basilisk_with_Flash_%2528debug%2529.zip/file). Then, go to `127.0.0.1:8080`.

Join our [Discord](https://discord.gg/jFyAePxDBJ) for questions and more information.

## Development

### Requirements:

- Java 24

### Running the Server

The server can be run in multiple ways:

- On Intellij IDE, by clicking run the `ApplicationKt` main function.
- Running the script `runserver.bat` or `runserver.sh`.
- Or in command line by:

```bash
.\gradlew run
```

- The server runs on:
  - `127.0.0.1:8080` (Web server + API server)
  - `127.0.0.1:7777` (Socket server)
  
- Open the game via a web browser **with native Flash support** (Ruffle is not supported yet). **You must go to `127.0.0.1:8080`**.

### Advanced

Our private server uses an in-memory, disk-backed database (minified JSON as store). This was made on our own without much optimization. Beside, our server supports MongoDB as the replacement. This is ideal for those that wants to host the game publicly and let a handful of people connect to it.

1. You need to install [MongoDB community edition](https://www.mongodb.com/products/self-managed/community-edition).
2. Run the MongoDB server manually (or via scripts: `runmongo.bat` and `runmongo.sh`).
3. MongoDB default connection is: `mongodb://localhost:27017/`. You can override it via the `MONGO_URL` environment variable.

> You don’t need to configure anything — by default, the server attempts to connect to MongoDB and automatically switches to the JSON database if it's not available.
