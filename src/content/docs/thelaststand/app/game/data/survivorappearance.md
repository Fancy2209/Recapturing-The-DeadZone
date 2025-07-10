---
title: SurvivorAppearance
slug: thelaststand/app/game/data/survivorappearance
description: SurvivorAppearance
---

Class that extends [HumanAppearance](/thelaststand/app/game/data/humanappearance) and is linked to a [Survivor](/thelaststand/app/game/data/survivor) object. It refers to [SurvivorLoadout](/thelaststand/app/game/data/survivorloadout) and manages human appearance based on the survivor class.

List of static string constants:

- `SLOT_UPPER_BODY` = `"clothing_upper"`
- `SLOT_LOWER_BODY` = `"clothing_lower"`

Internal methods contain:

- Loads `attire.xml`, setting a human appearance to a specific survivor class.
- Setting loadout and dispatching a signal whenever it changes.
- Serialize the class into `Object` which may have `skinColor`, `upper`, `lower`, `hair`, `facialHair`, `hairColor`, `forceHair`, `hideGear`.
