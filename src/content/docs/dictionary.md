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

### `_rootPath`

```actionscript-3
// preloader/thelaststand.preloader.core.Main`@line 188
this._rootPath = stage.loaderInfo.parameters.path || "";
```

Unsure what is or how is the actual link generated. It appears to be a boolean but the variable name somewhat doesn't fit a boolean. Need to know what is behind `stage.loaderInfo.parameters.path`. Albeit, this isn't really important as we never encountered an error related to this.

### `_localAssets`

A boolean flag used in the preloader, often referenced when the game attempts to load resources. It indicates whether the game should make a network request to the same domain as the `core.swf` file or download it from the network via [GameFS](/common/playerio/gamefs).

**Example usage:** `preloader/thelaststand.preloader.coreMain@line 81`.

It is declared in `preloader/thelaststand.preloader.coreMain@line 190`. More specifically, its value depends on the loader parameter, which is a data passed from the Flash variables (identified as `local`) in the website's JavaScript file (`main.js`).
