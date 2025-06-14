---
title: Sequential flow
slug: sequential_flow
description: The game process from start to end
---

This page shows a step-by-step of what is done to start the game.

If something is missing it may not have been documented yet.

## Flow

1. Load `index.html`
2. Handle login of the page
3. Embed Flash player with [preloader.swf file](/preloader-main)
4. Once the `preloader.swf` main ends, it make a request for the `core.swf` file starting the actual game
5. [Core.swf sequential flow](/core-main)
