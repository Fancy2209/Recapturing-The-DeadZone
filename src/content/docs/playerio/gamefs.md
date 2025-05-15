---
title: GameFS
slug: playerio/gamefs
description: GameFS
---

:::note
A backend service of PlayerIO. This is from https://playerio.com/documentation/services/gamefs/.
:::

`GameFS` is used to store and distribute game files to players.

## GameFS Constructor

Creates a new `GameFS` instance.

`GameFS(param1: String, param2: String)`

**Parameters:**

- **`param1` (string)**: `gameId` to uniquely identify file request. TLSDZ `gameId` is: `dev-the-last-stand-iret8ormbeshajyk6woewg`.
- **`param2` (string)**: `urlMap`, a mapping value for `param1`. Still unsure what it is used for. Example usage: `maps[param1] = UrlMap(param2)`

## Request

A file can be requested by using the `getUrl` method from the [`PlayerIO`](/playerio/playerio) client.

### `getUrl`

This method retrieves a URL for a file within the `GameFS`. It will convert the provided URL into a full internet URL.

**Parameters:**

- **`path` (string)**: The path to the file within the GameFS. (e.g., "mygame/images/logo.png").
- **`secure=false` (boolean)**: Indicates whether the returned URL should be using https or not.

**Return Value:**

- **`fullUrl` (string):** The full URL that can be downloaded.

**Example:**

`thelaststand.preloader.core.Main`@114:

```
PlayerIO.gameFs("dev-the-last-stand-iret8ormbeshajyk6woewg").getUrl(this._rootPath + _loc1_,this._useSSL)
```

:::note
DZ often uses `this._rootPath` before each URL. See the [dictionary](/dictionary#_rootpath).
:::

## Related Links:

- [PlayerIO](/playerio/playerio)
