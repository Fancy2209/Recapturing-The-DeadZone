---
title: TimerData
slug: thelaststand/app/game/data/timerdata
description: TimerData
---

Class that manages the in-game timer in various aspect of the game.

A timer has:

- `_target` (`*` type)
- `_timeStart`, `_timeEnd` (`Date` type)
- `_timeLength`, `_timeRemaining` (`Number` type)

A start, completion, or a cancellation of timer is handled via AS3 signal.

## readObject

1. `start: Number` — required, used to initialize `_timeStart` as a `Date`
2. `length: Number` — required, assigned to `_timeLength`
3. `data: Object` — optional, defaults to empty object `{}` if not provided.
   - Not entirely sure on the data format.
   - Any `NaN` number values inside `data` are coerced to `0`.
4. `calculateEndTime()` — internal method called at the end to finalize the timer's end time based on `start` and `length` values.

## speedUpByPurchaseOption

A method that speeds up timer from an in-game purchase. The input is an object which should either have the property `percent` or `time` (possibly in seconds).
