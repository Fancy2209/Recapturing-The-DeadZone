---
title: Intro
slug: index
description: Intro
---

## TLSDZ Documentation

Effort to create The Last Stand: Dead Zone private server. Document everything we know about the game.

- [DeadZone-Private-Server on GitHub](https://github.com/SulivanM/DeadZone-Private-Server)
- [DeadZone-Documentation on GitHub](https://github.com/glennhenry/DeadZone-Documentation)

## Our Progress

TLSDZ uses [PlayerIO backend service](https://playerio.com/). Our task is to design a server that response to the game request including making services that mimics PlayerIO backend.

If you do the installation instruction correctly, you should be at this screen:

![Last progress](../../assets/progress.png)

### What's Next?

You can help the development of the private server directly by making a change in the server code. You can also help us investigate, write helpful found, and let others develop the server.

Our general principle is:

1. Try to reach the point that our server is currently stuck at.
2. Investigate what is wrong and figure out what changes should be made to the server to progress further.
3. Document your findings or implement changes directly on the private server. Your finding may involve details about a specific piece of code, game packages or class, the game expected behavior without error, request or response specification, a known server issues, or possible solutions.
4. Repeat the process.

See [preloader](/preloader-main) and [core](/core-main) to know how the game works sequentially.

### Current Investigation

We have successfully serialized and deserialized message sent from client through our custom serde mechanism which is based on PlayerIO's. They likely defined their own serialization format, and we can't rely on msgpack. At least it worked for now.

Client is now fully connected to the socket server and is ready to exchange real-time data with the game. It is now on 'Loading your game' text, which probably mean it is trying to load the player's game data. We may need to play around with DB because this must include loading player's account from DB or establishing a new account where the game (IIRC) initiate "new survivor" creation to the player. Later, it should save the newly made account to the DB and proceed to the tutorial.

So, the next step is researching on how the game loads' data, what it expects, and possibly faking an account or data.

:::info
Any error from the client should be sent to the server via [API 50](/glossary#api-50).
:::

#### Note on Room

:::note
Room, in the context of PlayerIO means a separate instance of server side code. It is differentiated with room ID and room type. Different room may reside in different game servers and may be separated physically (detail about [PlayerIO room](https://playerio.com/documentation/services/multiplayer/essentials)).

In TLSDZ, there are game, chat, trade, and alliance type of room (`thelaststand.app.network.RoomType`). This means there are 4+ isolated instance of game servers that may run at a time (taking alliance into consideration).
:::
