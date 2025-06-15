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

We successfully loaded and unpacked all the assets after being stuck last time. Now it’s time for the client to connect to the server. The server connection is initiated by the core main calling the [`Network`](/thelaststand.app/network/network) class.

The client first makes an API request (with [API 27](/glossary#api-27)) to our API server. This request signals "CreateJoinRoom," indicating that the client wants to connect to the server.

:::note
Room, in the context of PlayerIO means a separate instance of server side code. It is differentiated with room ID and room type. Different room may reside in different game servers and is separated physically (detail about [PlayerIO room](https://playerio.com/documentation/services/multiplayer/essentials)).

In TLSDZ, there are game, chat, trade, and alliance type of room (`thelaststand.app.network.RoomType`). This means there are 4+ isolated instance of game servers that may run at a time (taking alliance into consideration).
:::

The server should respond appropriately by returning `roomId`, `joinKey`, and, most importantly, `ServerEndpoint`. The server endpoint contains the domain address and port for the client to connect.

We have successfully implemented API 27, and the client is now connected to our socket server. The first message we received after the connection establishment was the client sending back our API 27 response. We believe it's sort of a way that is done by the client to coordinate between the API and the socket server.

There are no additional messages after that, and the message is currently incomplete and cannot be deserialized. We are not sure what to do with the message (or if it's a deserialization issue on our server) and have yet to investigate the specific response to be sent next.
