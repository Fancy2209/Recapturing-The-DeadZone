---
title: Main
slug: core-main
description: Index page of the core.swf file
---

## Sequential flow

still TBD, WIP. currently, info are not detailed.

- Core loaded by preloader, starting at main class.
- Begin resource unpacking.
  - Start by initializing event listener for failure and successful unpacking.
  - Unpack and load resources in the background.
  - Notify that resource loading is done.
- Begin connection to socket server.
  - Start by calling API 27: CreateJoinRoom.
  - API server responds room data and the endpoint for the socket server.
  - Client connects to the socket server from the given endpoint and port.
  - Client sends a null byte (probably to indicate message start).
  - Client sends the CreateJoinRoom response data it receives from API 27.
  - Socket server responds with a `playerio.joinresult` with either true (success) or false (failure with an error message) depending on whether the join data is valid or not.
  - Client verifies that it was a success and dispatches a "connected" event that marks it has successfully connected to the socket server and ready to progress further.
- `onGameReady` function from [Network.as](/thelaststand.app/network/network) runs.
  - This function expects a message of length 5 and the type "gr". It includes the type itself, server time, binaries data (XML files from server), and 3 JSON dumps which are cost table data, survivor class table, and login player state.
  - The socket server should send this message.
  - The `onNetworkGameDataReceived` from core `Main.as` will be called every time the game receives XML files from server. The function simply load and update the currently used XML with the newly received XML.
  - The JSON dumps will be parsed, and its data will be stored in local variable of Network class for later uses.
  - Next is loading `PlayerObjects` from BigDB. A network error (which force disconnect the player) will be raised if it fails.

### Request made to API server

Order of call:

1. 27 (CreateJoinRoom)
