---
title: Morale
slug: thelaststand/app/game/data/morale
description: Morale
---

The class that tracks morale effect of survivor, which is the productiveness or simply the happiness value. It is affected by the number of completed missions, [injury](/thelaststand/app/game/data/injury/injury), comfort, food and water stocks, etc.

It also includes methods that retrieve or set effects, calculate the total morale, applying a specific multiplier, and rounding.

List of static string constants:

- `Morale.EFFECT_INJURY` = `"injury"`
- `Morale.EFFECT_MISSION_COMPLETE` = `"missionComplete"`
- `Morale.EFFECT_FOOD` = `"food"`
- `Morale.EFFECT_WATER` = `"water"`
- `Morale.EFFECT_SECURITY` = `"security"`
- `Morale.EFFECT_COMFORT` = `"comfort"`
- `Morale.EFFECT_AVERAGE_SURVIVOR` = `"avgSurvivor"`
- `Morale.EFFECT_DAILY_QUEST_COMPLETED` = `"dailyQuestCompleted"`
- `Morale.EFFECT_DAILY_QUEST_FAILED` = `"dailyQuestFailed"`

## readObject

Read list of property of a plain object and populate a local dictionary, `_effects`. Each property becomes a key in `_effects`, and its value is converted to a `Number`.

There is also a `getEffect` method that attempts to retrieve an effect from the dictionary using a specified key.

The two likely involves the use of `Morale` static constants.
