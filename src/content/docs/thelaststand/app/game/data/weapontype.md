---
title: WeaponType
slug: thelaststand/app/game/data/weapontype
description: WeaponType
---

The type of weapons in TLSDZ, not to be confused with [weapon class](/thelaststand/app/game/data/weaponclass).

List of static _unsigned integer_ constants:

- `NONE` = 0
- `AUTO` = 1
- `SEMI_AUTO` = 2
- `ONE_HANDED` = 4
- `TWO_HANDED` = 8
- `IMPROVISED` = 16
- `EXPLOSIVE` = 32
- `BLADE` = 64
- `BLUNT` = 128
- `AXE` = 256
- `SPECIAL` = 512

Usage in [`SurvivorClass`](/thelaststand/app/game/data/survivorclass): A survivor class can be proficient at multiple types of weapons, with its specialties represented as bitmask where each bit corresponds to a `WeaponType` constants.
