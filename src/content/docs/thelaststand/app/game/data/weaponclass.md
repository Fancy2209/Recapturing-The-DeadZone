---
title: WeaponClass
slug: thelaststand/app/game/data/weaponclass
description: WeaponClass
---

The class of weapons in TLSDZ, not to be confused with [weapon type](/thelaststand/app/game/data/weapontype).

List of static string constants:

- `WeaponClass.ASSAULT_RIFLE` = `"assault_rifle"`
- `WeaponClass.BOW` = `"bow"`
- `WeaponClass.LAUNCHER` = `"launcher"`
- `WeaponClass.LONG_RIFLE` = `"long_rifle"`
- `WeaponClass.MELEE` = `"melee"`
- `WeaponClass.PISTOL` = `"pistol"`
- `WeaponClass.SHOTGUN` = `"shotgun"`
- `WeaponClass.SMG` = `"smg"`
- `WeaponClass.LMG` = `"lmg"`
- `WeaponClass.THROWN` = `"thrown"`
- `WeaponClass.HEAVY` = `"heavy"`

Usage in [`SurvivorClass`](/thelaststand/app/game/data/survivorclass): A survivor class can equip multiple types of weapons, which is why `SurvivorClass` contains an array of `WeaponClass`.
