# Recapturing The DeadZone

TLS:DZ private server.

## Development

Requirements:
- Java 21, Gradle 8.5
- MongoDB community edition

1. Run the mongodb first by running `runmongo.bat`.
2. The server can be run from:
    - From Intellij IDE, clicking run on ApplicationKt
    - Or by running the `runserver.bat`

## TODO

Socket message to implement:
- `s` (save)
  - has field `data` and `id`. data contains type
  - response for this message is an object (the data depends on the sender) with message type `ss` (save success).
  - types:
    - [NOT IMPORTANT] type `get_offers`. initiated by `OfferSystem` to get offers.
    - [NOT IMPORTANT] type `chat_getContactsBlocks`. initiated by `ChatSystem` to get player's contacts and blocklist.
- [NOT IMPORTANT] `rqa` (requested zombie attack, initiated from client using Cc command)
- `ic` (init complete): unknown what server should do. client doesn't have listener for response of `ic` message. probably client only letting server know?

API to implement:
- 30
