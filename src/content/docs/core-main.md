---
title: Main
slug: core-main
description: Index page of the core.swf file
---

Always a WIP.

### Game Bootstrap

- The game starts with the Main class, which is invoked by the [preloader](/preloader-main).
- Begin resource unpacking.
  - Event listeners are registered for successful and failed unpacking events.
  - Resources are unpacked and loaded asynchronously in the background.
  - Once completed, a notification is dispatched indicating that resource loading is done.

### Socket Server Connection Establishment

- Begin connection to socket server.
  - This is triggered by calling the `connect()` method on the [Network](/thelaststand/app/network/network) class, which internally invokes a private `joinRoom()` method.
  - `joinRoom()` uses the PlayerIO [Multiplayer](/playerio/multiplayer) API to request [create and join room (API 27)](/glossary#api-27).
  - API server responds room data and the endpoint for the socket server.
  - Client connects to the socket server from the given endpoint and port.
  - Client sends the initial `"join"` message which includes the data received from API 27.
  - The socket server replies with a `playerio.joinresult` message:
    - If the join is valid, send a true boolean value.
    - If the join is invalid, send false along with an error message.
  - Upon success, a connected event is dispatched, indicating the socket connection is ready for communication.

### Initial Data Exchange & Player Data Loading

- Early socket communication is focused on retrieving the player's game state.
- [`onGameReady`](/thelaststand/app/network/network#ongameready) function from Network class is expected to run.
  - The `"gr"` message (game ready) should be sent from the socket server:
    - The message contains 5 values: server time, binaries data (XML files from server), and 3 JSON dumps which are cost table data, survivor class table, and login player state. The message is allegedly specific to the authenticated player account.
  - The `onNetworkGameDataReceived` from core `Main.as` will be triggered every time the game receives XML files from server. This method updates the game's currently loaded XML with the newly received XML.
  - The JSON dumps are parsed and stored in local variables in the `Network` class for later use.
  - Next is loading `PlayerObjects`, `NeighborHistory`, and `Inventory` from BigDB by making request to [API 85](/glossary#api-85). A network error (which force disconnects the player) will be raised if the loaded objects are empty or null.
- When all three objects are loaded successfully, [`onPlayerDataLoaded`](/thelaststand/app/network/network#onplayerdataloaded) is called.
- `onPlayerDataLoaded`
  - Construct in-game `Survivor` objects.
  - Parses the loaded `PlayerObjects` and initializes internal game state.
  - Initializes core game systems: `GlobalQuestSystem`, `QuestSystem`, `OfferSystem`, and `AllianceSystem`.
  - Send the game ready signal.

### Game Start

- The `onNetworkGameReady` method in main listens for the game ready signal and calls the `onReady` method next, which:
  - Closes the loading dialogue and removes network listeners for loading/error/start connection events.
  - Opens the "create survivor" dialogue (if necessary).
