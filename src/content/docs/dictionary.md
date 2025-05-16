---
title: Dictionary
slug: dictionary
description: Dictionary
---

Contains every little, uncategorized information that is often used in the game.

## Networking Related

### `gameId`

The `gameId` of TLS:DZ is `dev-the-last-stand-iret8ormbeshajyk6woewg`. It is typically used for URL retrieval in [`GameFS`](/playerio/gamefs).

### `_rootPath`

```actionscript-3
// preloader/thelaststand.preloader.core.Main`@line 188
this._rootPath = stage.loaderInfo.parameters.path || "";
```

Unsure what is or how is the actual link generated. It appears to be a boolean but the variable name somewhat doesn't fit a boolean. Need to know what is behind `stage.loaderInfo.parameters.path`. Albeit, this isn't really important as we never encountered an error related to this.
