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

### Investigation

The error message says something about `publishingnetworklogin` and the requirement of a script called `publishingnetwork.js`.

Each auth system (Kong/AG/FB/PIO) is managed differently. PlayerIO authentication is associated with the [publishing network](/playerio/publishingnetwork).

A game website that is registered in the publishing network requires a canvas to enable game to run. This canvas needs a script the `publishingnetwork.js` to function.

In our scenario, by using the PlayerIO authentication, we are trying to make our web server to operate as a registered publishing network site. This involves creating the canvas and including the script. We may also need to register with the publishing network to obtain credentials for authentication.

Our next task could be modifying the web server to be mimicking a valid publishing network site. The PlayerIO authentication will succeed if we see an API request to `/api/601` endpoint.

### Technical Insights & Troubleshooting

During our investigation, we've uncovered some crucial details that are valuable for general development and troubleshooting, particularly concerning the Flash-JavaScript bridge:

- **`Security.allowDomain` Restriction:** It has been discovered that `Security.allowDomain` in `preloader/main.as@line 82` hardcodes the allowed domain for JavaScript callbacks to a specific address, `ddeadzonegame.com`. If the domain where the SWF is loaded differs from this hardcoded value, the JavaScript callbacks (which are essential for communication between Flash and the web page) will not work.
- **Domain Manipulation Options:** The allowed domain can be influenced by setting specific Flash variables when creating the player:
    - If `local` is set to `1`, the Flash application will utilize the domain of the `core.swf` file (passed in the Flash variables) for its domain checks.
- **Current Workaround:** As a temporary solution, using the IP address `127.0.0.1` instead of `localhost` seems to resolve the domain issue without requiring any modifications.
