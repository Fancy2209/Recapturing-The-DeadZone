---
title: Glossary
slug: glossary
description: Glossary
---

Contains every little, uncategorized information that is often used in the game.

## Networking Related

### `gameId`

`gameId` is received by registering an account (and creating a game) at [PlayerIO](playerio.com). The `gameId` of TLS:DZ is `dev-the-last-stand-iret8ormbeshajyk6woewg`. It is used to communicate with PlayerIO backend services, such as URL retrieval in [`GameFS`](/playerio/gamefs).

### `_localAssets`

A boolean flag used in the preloader, often referenced when the game attempts to load resources. It indicates whether the game should make a network request to the same domain as specified in the `core.swf` file or download it from the network via [GameFS](/playerio/gamefs).

**Example usage:** `preloader/thelaststand.preloader.coreMain@line 81`.

This flag is declared in the `onAddedToStage` method at `preloader/thelaststand.preloader.coreMain@line 190`. Its value is determined by the loader parameter, which is passed from the flash variables defined in the website's JavaScript file (`main.js`).

:::tip
Other properties of the preloader `Main` such as `_rootPath`, `_coreFile`, and `_useSSL` are also derived from the flash variables.
:::

## API Requests

TLSDZ utilizes RPC to make requests to the API server by invoking the `Request` method in [`HTTPChannel`](/playerio/utils/httpchannel). Additionally, both requests and responses are formatted using Protobuf.

All expected messages for input (arguments), output, and errors can be found in the `playerio.generated.messages` package. The server follows the input message format for making requests, and our server must respond to the API with the expected output message. The error format is used by the client and typically only contains error code and message.

Below is a list of API requests made by the game, along with the expected messages defined in the `.proto` file.

### API 13

Authenticate. Contextual call `thelaststand.app.network.PlayerIOConnector@line 210` and actual call `playerio.generated.PlayerIO@line 68`.

Response is based on `AuthenticateOutput` message.

```protobuf
message AuthenticateOutput {
  optional string token = 1;
  optional string userId = 2;
  optional bool showBranding = 3 [default = false];
  optional string playerInsightState = 4;
  optional bool isSocialNetworkUser = 5 [default = false];
  optional bool isInstalledByPublishingNetwork = 6 [default = false];
  optional bool deprecated1 = 7 [default = false];
  optional string apiSecurity = 8;
  repeated string apiServerHosts = 9;
}
```

### API 601

Social refresh. Contextual call `thelaststand.app.network.PlayerIOConnector@line 221` and actual call `playerio.generated.social@line 33`.

Response is based on `SocialRefreshOutput` message.

```protobuf
message SocialRefreshOutput {
    optional SocialProfile myProfile = 1;
    repeated SocialProfile friends = 2;
    repeated string blocked = 3;
}

message SocialProfile {
    optional string userId = 1;
    optional string displayName = 2;
    optional string avatarUrl = 3;
    optional bool lastOnline = 4;
    optional string countryCode = 5;
    optional string userToken = 6;
}
```

### API 27

Create join room. Contextual call `thelaststand.app.network.Network@line 1064` and actual call `playerio.generated.Multiplayer@line 155`.

Response is based on `CreateJoinRoomOutput` message.

```protobuf
message CreateJoinRoomOutput {
  optional string roomId = 1;
  optional string joinKey = 2;
  repeated ServerEndpoint endpoints = 3;
}

message ServerEndpoint {
  optional string address = 1;
  optional int32 port = 2;
}
```
