---
title: PlayerIOConnector
slug: playerio/playerioconnector
description: PlayerIOConnector
---

:::note
Part of PlayerIO backend services.
:::

`PlayerIOConnector` is a service that authenticate the players. It can connect to Facebook, Armor games, Kongregate, or even through PlayerIO itself (probably for developers).

After the [preloader](/main#preloader) loads completely (`thelaststand.preloader.core.Main@162`), the game starts communicating with `PlayerIOConnector`. The communication decides which authentication service to use.

Currently, our private server choose to authenticate through PlayerIO.

## Authenticate by PlayerIO

```actionscript-3
// thelaststand.app.network.PlayerIOConnector@line 209
PlayerIO.authenticate(stage,GAME_ID,"publishingnetwork",{"userToken":userToken},null,function(param1:Client):void {
    onPlayerIOPubNetworkConnected(param1,userToken);
},this.onConnectError);
```

It tries to authenticate through the [`PlayerIO`](/playerio/playerio) client library and also set up an event listener. The listener seem to be interacting with player's data(?) through the `refresh` method in the [`PublishingNetwork`](/playerio/publishingnetwork) class.
