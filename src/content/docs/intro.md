---
title: Intro
slug: index
description: Intro
---

### TLSDZ Documentation

Effort to create The Last Stand: Dead Zone private server. Document everything we know about the game.

- [DeadZone-Private-Server on GitHub](https://github.com/SulivanM/DeadZone-Private-Server)
- [DeadZone-Documentation on GitHub](https://github.com/glennhenry/DeadZone-Documentation)

### Our Progress

If you follow the development instruction correctly, you should be at this screen:

![Last progress](../../assets/progress.png)

#### What's Next?

Not entirely sure what is `publishingnetworklogin`. It could be a mechanism to indicate whether we are logging in through publisher services like Kong/AG/FB. The game set it to `:auto` probably because we are logging in through PlayerIO (as if we are looking at a login bypass mechanism). However, what `publishingnetwork.js` could be?

Things to look at:

- `connectViaPlayerIO()`: `preloader/thelaststand.app.network.PlayerIOConnector`@line 205.
- `PlayerIO.authenticate()`: `preloader/playerio.PlayerIO`@line 33 and @line 120.
- `PublishingNetwork()`: `preloader/playerio.PublishingNetwork`@line 19 and @line 54.
