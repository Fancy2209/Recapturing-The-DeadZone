# Recapturing The DeadZone

TLS:DZ private server (revival).

## How to Play?

Please join our [Discord](https://discord.gg/jFyAePxDBJ) for more information.

## Development

### Requirements:

- Java 24
- Gradle 8.14

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

Our private server supports MongoDB as the replacement for the embedded database. This is ideal for those that wants to host the game publicly and let a handful of people connect to it.

1. You need to install [MongoDB community edition](https://www.mongodb.com/products/self-managed/community-edition).
2. Run the MongoDB server manually (or via scripts: `runmongo.bat` and `runmongo.sh`).
3. MongoDB default connection is: `mongodb://localhost:27017/`. You can override it via the `MONGO_URL` environment variable.

> You don’t need to configure anything — by default, the server attempts to connect to MongoDB and automatically switches to the embedded database if it's not available.
