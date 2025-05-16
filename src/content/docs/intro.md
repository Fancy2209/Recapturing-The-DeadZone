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

If you follow the development instruction correctly, you should be at this screen:

![Last progress](../../assets/progress.png)

### What's Next?

You can help the development of the private server directly by making a change in the code. You can also help us investigate and let others develop the server.

Our general principle is:

1. Try to reach the point that our server is currently stuck at.
2. Investigate what is wrong and figure out what changes should be made to the server to progress further.
3. Write your research or make the changes directly.
4. Repeat the process.

We have been documenting the sequential flow of the game in [main](/main). It's a good start to see where are we currently at. Right now, we are stuck in the authentication process.

---

The error message says something about `publishingnetworklogin`. It could be a mechanism to denote whether are we logging in through publisher services like Kong/AG/FB. Because we are logging in through PlayerIO, the game probably set it to `:auto` (as if we are looking at a login bypass mechanism). However, we are missing that `publishingnetwork.js`. It could be a script that takes care of the login requirement. That script isn't included anywhere in the game files(?). It's possible that it's a PlayerIO related script that we must create our own(?).

Decompile the game and take a look at the code. Things to look at:

- `connectViaPlayerIO()`: `preloader/thelaststand.app.network.PlayerIOConnector`@line 205.
- `PlayerIO.authenticate()`: `preloader/playerio.PlayerIO`@line 33 and @line 120.
- `PublishingNetwork()`: `preloader/playerio.PublishingNetwork`@line 19 and @line 54.
