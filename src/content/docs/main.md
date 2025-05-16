---
title: Main
slug: main
description: Main
---

TLS:DZ uses [PlayerIO backend service](https://playerio.com/). Our task is to design a server that mimics it.

The game has `core.swf` (actual game) and `preloader.swf` (loading screen).

## Preloader

The preloader is what first loaded when we start the game. It does all the necessary preparation before loading the game, such as setting up event listener, networking, and authentication (e.g., AG, Kong, FB).

It is the “loading screen” of the game. If the browser successfully loads `preloader.swf`, it should show us this loading screen.

![Loader screen](../../assets/loaderbg.png)

After the loader screen is loaded, the game starts the authentication process through the [`PlayerIOConnector`](/playerio/playerioconnector) class.

If the player successfully authenticate, the next step would be loading the core.

## Core

Core loading starts from locating and downloading the `core.swf` file. Once it is downloaded, the game switches to `core.swf`.

Behind the scene, it does the necessary things such as showing load animation to player, running sound effects or music, and installing input listener (mouse, keyboard). Then, it proceeds to loading the game assets, that is extracting the game files under `/data`, such as:

- `resource_main.xml`: the resource locator, loads other resources.
- `resources_mission.xml`: contains mission assets.
- `pak1.zip`: generic assets
- `pak2.zip`: sound assets
- and so on...

Once everything is unpacked, the game installs network event listener and start connecting to the server.
