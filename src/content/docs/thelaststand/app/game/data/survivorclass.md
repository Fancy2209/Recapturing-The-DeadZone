---
title: SurvivorClass
slug: thelaststand/app/game/data/survivorclass
description: SurvivorClass
---

The detailed specification of each [survivor class](/thelaststand/app/game/data/survivorclass), containing the class ID, [attributes](/thelaststand/app/game/data/attributes), 3D assets URI, and weapon class.

List of static string constants:

- `SurvivorClass.FIGHTER` = `"fighter"`
- `SurvivorClass.MEDIC` = `"medic"`
- `SurvivorClass.SCAVENGER` = `"scavenger"`
- `SurvivorClass.ENGINEER` = `"engineer"`
- `SurvivorClass.RECON` = `"recon"`
- `SurvivorClass.PLAYER` = `"player"`
- `SurvivorClass.UNASSIGNED` = `"unassigned"`

The exclusive skill owned by each class:

- Fighter: `[Attributes.COMBAT_PROJECTILE, Attributes.COMBAT_MELEE, Attributes.COMBAT_IMPROVISED]`
- Recon: `[Attributes.TRAP_SPOTTING, Attributes.COMBAT_PROJECTILE]`
- Engineer: `[Attributes.TRAP_DISARMING, Attributes.COMBAT_IMPROVISED, Attributes.COMBAT_MELEE]`
- Scavenger: `[Attributes.SCAVENGE_SPEED]`
- Medic: `[Attributes.HEALING]`

## readObject

A method to read an AS3 object type to populate the class.

1. `id: String` — required
2. `maleUpper: String` — required (and other similar strings are likely the assets URI)
3. `maleLower: String` — required
4. `maleSkinOverlay: String` — optional (nullable, assigned only if not null)
5. `femaleUpper: String` — required
6. `femaleLower: String` — required
7. `femaleSkinOverlay: String` — optional (nullable, assigned only if not null)
8. `baseAttributes: Attributes` — required (calls [`readObject() of Attributes`](/thelaststand/app/game/data/attributes#readobject))
9. `levelAttributes: Attributes` — required (calls [`readObject() of Attributes`](/thelaststand/app/game/data/attributes#readobject))
10. `hideHair: Boolean` — optional (defaults to `false` if not present)
11. `weapons.classes: Array` — optional (if present, strings are mapped to [`WeaponClass`](/thelaststand/app/game/data/weaponclass) constants)
12. `weapons.types: Array` — optional (if present, strings are mapped into [`WeaponType`](/thelaststand/app/game/data/weapontype) class and OR'ed with the corresponding bitmask)
