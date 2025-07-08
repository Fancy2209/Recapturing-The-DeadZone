---
title: Injury
slug: thelaststand/app/game/data/injury/injury
description: Injury
---

The class that tracks a survivor's injuries. Injuries data is based on the `injury.xml` resource file.

According to `injury.xml`:

Injuries have a severity: minor, moderate, serious, severe, and critical; each with rarity, morale reduction, and expire time. There are multiple types of injuries, such as fractures, which can result from blunt or sharp forces and have a rarity of 500. A skull fracture reduces the skills `combatMelee`, `combatProjectile`, and `combatImprovised` to 0.95. It can be treated with a bandage.

## readObject

1. `id: String` — required
2. `type: String` — required
3. `location: String` — required
4. `severity: String` — required
5. `damage: Number` — required
6. `morale: Number` — required
7. `timer: Object` — optional, if provided initializes a [`TimerData`](/thelaststand/app/game/data/timerdata) object and registers it unless expired

Additionally, internal logic that looks up `severity` inside the XML, also populate additional effects of injury to `_effects` dictionary.

## InjuryCause and InjurySeverity

The two classes of the injury package only contains static string constants.

- `InjuryCause.UNKNOWN` = `"unknown"`
- `InjuryCause.BLUNT` = `"blunt"`
- `InjuryCause.SHARP` = `"sharp"`
- `InjuryCause.HEAT` = `"heat"`
- `InjuryCause.BULLET` = `"bullet"`
- `InjuryCause.ILLNESS` = `"illness"`

---

- `InjurySeverity.MINOR` = `"minor"`
- `InjurySeverity.MODERATE` = `"moderate"`
- `InjurySeverity.SERIOUS` = `"serious"`
- `InjurySeverity.SEVERE` = `"severe"`
- `InjurySeverity.CRITICAL` = `"critical"`
