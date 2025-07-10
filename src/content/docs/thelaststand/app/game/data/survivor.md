---
title: Survivor
slug: thelaststand/app/game/data/survivor
description: Survivor
---

Survivor representation of TLSDZ, contains property like:

- XP and XP for next level.
- Movement state (e.g., run, walk).
- [SurvivorLoadout](/thelaststand/app/game/data/survivorloadout)
- [InjuryList](/thelaststand/app/game/data/injury/injurylist).
- [Morale](/thelaststand/app/game/data/morale).
- [SurvivorClass](/thelaststand/app/game/data/survivorclass)
- [SurvivorAppearance](/thelaststand/app/game/data/survivorappearance), which defines the looks of survivor visually, based on loadout and accessories.
- [Attributes](/thelaststand/app/game/data/attributes), the detailed stats of a survivor, such as the skill of healing, movement, scavenge, trap spotting, etc.
- And many AS3 signal, such as `classChanged`, `taskChanged`, `levelIncreased`, `xpIncreased`. They are dispatched correspondingly.

## readObject

While the class constructor initialize the `Survivor` object, the `readObject` is used to parse a raw survivor object (usually an AS3 object) and populate its local properties.

Based on `thelaststand.app.game.data/Survivor.as/line@ 701`, the list of properties the object is expected to have:

1. `id: String` — required
2. `title: String` — required
3. `firstName: String` — optional (defaults to `""` if not present)
4. `lastName: String` — optional (defaults to `""` if not present)
5. `gender: String` — required (uses [`Gender`](/thelaststand/app/game/data/gender) constants)
6. `portrait: String` (nullable) — optional (assigned to `_portraitURI`, `null` if not present)
7. `classId: String` — required (uses [`SurvivorClass`](/thelaststand/app/game/data/survivorclass) constants, may need to be prefixed with `class_`). This will derive class which is an object of `SurvivorClass`. Also derive [attributes](/thelaststand/app/game/data/attributes) based on its `readObject()` from the `SurvivorClass`.
8. `morale: Morale` — optional (calls [`readObject() of Morale`](/thelaststand/app/game/data/morale#readobject))
9. `injuries: InjuryList` — optional (calls [`readObject of InjuryList`](/thelaststand/app/game/data/injury/injurylist#readobject))
10. `level: int` — required
11. `xp: int` — required
12. `missionId: String` — optional (assigned and sets the local state to `ON_MISSION` if present)
13. `assignmentId: String` — optional (assigned and sets the local state to `ON_ASSIGNMENT` if present)
14. `reassignTimer: TimerData` — optional (calls [`readObject() of TimerData`](/thelaststand/app/game/data/timerdata#readobject)). Only set if present and the `TimerData` method of `hasEnded()` equal false.
15. `appearance: HumanAppearance` — required
    - uses `copyFrom()` if already a [`HumanAppearance`](/thelaststand/app/game/data/humanappearance)
    - otherwise deserialized using `deserialize(gender, appearance)`
16. `scale: Number` — optional (default: `1.22` for female, `1.25` otherwise)
17. `voice: String` — optional.

:::note
This doesn't include additional internal logic.
:::
