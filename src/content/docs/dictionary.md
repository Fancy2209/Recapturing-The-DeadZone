---
title: Dictionary
slug: dictionary
description: Dictionary
---

Contains every little, uncategorized information that is often used in the game.

## Networking Related

### `gameId`

The `gameId` of TLS:DZ is `dev-the-last-stand-iret8ormbeshajyk6woewg`. It is used to communicate with PlayerIO backend services, such as URL retrieval in [`GameFS`](/common/playerio/gamefs).

:::note
`gameId` is received by registering an account (and creating a game) at [PlayerIO](playerio.com). The `gameId` we know is not the original TLS:DZ ID, but rather produced from one of our devs.
:::

### `_localAssets`

A boolean flag used in the preloader, often referenced when the game attempts to load resources. It indicates whether the game should make a network request to the same domain as the `core.swf` file or download it from the network via [GameFS](/common/playerio/gamefs).

**Example usage:** `preloader/thelaststand.preloader.coreMain@line 81`.

This flag is declared in the `onAddedToStage` method at `preloader/thelaststand.preloader.coreMain@line 190`. Its value is determined by the loader parameter, which is passed from the flash variables defined in the website's JavaScript file (`main.js`).

:::tip
Other properties of the preloader `Main` such as `_rootPath`, `_coreFile`, and `_useSSL` are also derived from the flash variables.
:::
