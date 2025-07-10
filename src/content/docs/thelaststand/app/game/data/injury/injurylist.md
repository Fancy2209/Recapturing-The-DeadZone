---
title: InjuryList
slug: thelaststand/app/game/data/injury/injurylist
description: InjuryList
---

Class that tracks a list of [injuries](/thelaststand/app/game/data/injury/injury) affecting a survivor.

Internal logic includes methods such as `addInjury`, `removeInjury`, `getTotalDamage`, `getHealAllCost`, and `getTotalMorale`, etc.

## readObject

1. `param1: Array` — required, must be an `Array` of [injury](/thelaststand/app/game/data/injury/injury) data objects; if not an array, the function exits early

Internally:

- Each non-null element in the array is passed to a new `Injury` instance through its [`readObject`](/thelaststand/app/game/data/injury/injury#readobject) method.
- A one-time event listener, `timerCompleted`, is added for each injury's timer to manage its completion.
- Injuries are added to the local variable list and indexed in a dictionary using their `id`.
