# DeadZone Documentation

Repository for the [DeadZone Documentation](https://dead-zone-documentation.vercel.app/) website.

The private server repository: [DeadZone Private Server](https://github.com/SulivanM/DeadZone-Private-Server).

To run the website locally, clone the repo and then:

```
npm install
npm run dev
```

### How to add new page:

A page must be `.md` file and is enforced to have this on top of them (frontmatter):

```
---
title: Subfolder Example
slug: playerio/subfolder-example/subfolder
description: example
---
```

Replace the title, slug, and description appropriately. The slug follows the folder structure. Any images or videos are placed on `src/assets/`.

Then, add the page on sidebar by:

1. Start by editing the `astro.config.mjs`.
2. Follow the previous sidebar links pattern. Depending on the page topic, you can create a new sidebar groups or place it inside the defined groups.
