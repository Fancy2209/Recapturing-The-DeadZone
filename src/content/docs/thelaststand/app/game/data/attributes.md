---
title: Attributes
slug: thelaststand/app/game/data/attributes
description: Attributes
---

Representation of [survivors](/thelaststand/app/game/data/survivor) skill attributes.

List of static string constants:

- `Attributes.COMBAT_IMPROVISED` = `"combatImprovised"`
- `Attributes.COMBAT_PROJECTILE` = `"combatProjectile"`
- `Attributes.COMBAT_MELEE` = `"combatMelee"`
- `Attributes.MOVEMENT_SPEED` = `"movement"`
- `Attributes.SCAVENGE_SPEED` = `"scavenge"`
- `Attributes.HEALING` = `"healing"`
- `Attributes.TRAP_SPOTTING` = `"trapSpotting"`
- `Attributes.TRAP_DISARMING` = `"trapDisarming"`
- `Attributes.HEALTH` = `"health"`
- `Attributes.INJURY_CHANCE` = `"injuryChance"`

## writeObject

This method is used to create a default object of `Attributes`, initializing each property to its default value, which are:

- `health` = 1;
- `combatProjectile` = 1;
- `combatMelee` = 1;
- `combatImprovised` = 1;
- `movement` = 1;
- `scavenge` = 1;
- `healing` = 0;
- `trapSpotting` = 0;
- `trapDisarming` = 0;
- `injuryChance` = 0;

## readObject

This method reads the `Attributes` in an object format. It initializes each `Attributes` property based on the values provided in the object, with a fallback value of 0.
