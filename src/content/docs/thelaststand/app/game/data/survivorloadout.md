---
title: SurvivorLoadout
slug: thelaststand/app/game/data/survivorloadout
description: SurvivorLoadout
---

Class representing a survivor's loadout. Own reference to weapon, passive gear, and active gear, with all being the type of `SurvivorLoadoutData`.

`SurvivorLoadoutData` class is none other than a data container for a single loadout entry. It has a type based on a string, an [item](/thelaststand/app/game/data/item), and a number of quantity. If something is altered, the `changed` signal is dispatched.

List of static string constants:

- `SurvivorLoadout.SLOT_WEAPON` = `"weapon"`
- `SurvivorLoadout.SLOT_GEAR_PASSIVE` = `"gearPassive"`
- `SurvivorLoadout.SLOT_GEAR_ACTIVE` = `"gearActive"`
- `SurvivorLoadout.TYPE_OFFENCE` = `"offence"`
- `SurvivorLoadout.TYPE_DEFENCE` = `"defence"`

Internal methods contain:

- Logic that retrieves XML data
- Manages loadout and returning required data
- Enforcing survivor level/carry limit for an item
