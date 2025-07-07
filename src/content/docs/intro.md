---
title: Intro
slug: index
description: Intro
---

## TLSDZ Documentation

Effort to create The Last Stand: Dead Zone private server. Document everything we know about the game.

- [DeadZone-Private-Server on GitHub](https://github.com/SulivanM/DeadZone-Private-Server)
- [DeadZone-Documentation on GitHub](https://github.com/glennhenry/DeadZone-Documentation)

TLSDZ uses [PlayerIO backend service](https://playerio.com/). Our task is to design a server that response to the game request including making services that mimics PlayerIO backend.

## Our Progress

This is the furthest we have been (the latest change may have not been approved yet):

![Last progress](../../assets/progress.png)

### What's Next?

You can help the development of the private server directly by making a change in the server code. You can also help us investigate, write helpful found, and let others develop the server.

Our general principle is:

1. Try to reach the point that our server is currently stuck at.
2. Investigate what is wrong and figure out what changes should be made to the server to progress further.
3. Document your findings or implement changes directly on the private server. Your finding may involve details about a specific piece of code, game packages or class, the game expected behavior without error, request or response specification, a known server issues, or possible solutions.
4. Repeat the process.

See [preloader](/preloader-main) and [core](/core-main) to know how the game works sequentially. Checkout the GitHub issues for the private server or ask questions away in our [Discord](https://discord.gg/Q5dTKrPmfq).

### Current Investigation

We have implemented [API 85](/glossary#api-85) with the response of mocked data. As a result, the game throw an NPE when calling the method `readObject` of Network class to the `PlayerObjects`. This means the game expects a data field to be present in the object, but it actually isn't in our mocked data.

To fix this, we need to know how `readObject` method works, and what it reads/expects for the `PlayerObjects`. The player object is a nested object of player's data and preference. Some data is another object which may have their own `readObject`. We will need to trace each `readObject` call and find out which data field is missing.

:::tip
After connected to socket server, client errors are sent to the server through [API 50](/glossary#api-50). In our private server, these errors are logged in `write_error.log`. This is particularly helpful for debugging, as the flash debugger does not always report errors. Additionally, you can modify the SWF to intentionally trigger an error, which will help you trace the issue.

`Logch` is command is also useful to send log to the console of the game (can be activated by pressing `=`). Log command can be written like `Cc.logch("<identifier>", "<logmsg>")`.
:::
