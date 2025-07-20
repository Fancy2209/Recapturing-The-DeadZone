---
title: DynamicQuest
slug: thelaststand/app/game/data/quests/dynamicquest
description: DynamicQuest
---

DynamicQuest class

## Object structure

```scala
data DynamicQuest

raw: ByteArray! // see DynamicQuest.as for detail of structure

quest: Quest! // inherited

questType: Int!
accepted: Boolean!
goals: List<DynamicQuestGoal> = []
rewards: List<DynamicQuestReward> = []
failurePenalties: List<DynamicQuestPenalty> = []

```
