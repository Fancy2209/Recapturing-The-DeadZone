---
title: HumanAppearance
slug: thelaststand/app/game/data/humanappearance
description: HumanAppearance
---

Represent a generic human appearance (e.g., extended by [SurvivorAppearance](/thelaststand/app/game/data/survivorappearance)). It has [`AttireData`](/thelaststand/app/game/data/attiredata) of skin, hair, hair color, facial hair, upper and lower body, accessories, base attire, overlay, and more.

A change in appearance dispatch the changed signal.

Internal methods contain:

- Reading XML assets and populating properties.
- The `deserialize` method loads the `attire.xml` assets and set appearance attributes based on the XML definitions.
