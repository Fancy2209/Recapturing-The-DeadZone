// @ts-check
import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";
import starlightThemeObsidian from "starlight-theme-obsidian";
import vercel from "@astrojs/vercel";

// https://astro.build/config
export default defineConfig({
  adapter: vercel({
    imageService: true,
    devImageService: "sharp",
  }),
  base: process.env.NODE_ENV === "production" ? "/" : "/",
  integrations: [
    starlight({
      plugins: [starlightThemeObsidian()],
      favicon: "icon.ico",
      customCss: ["./src/assets/custom.css"],
      tableOfContents: { minHeadingLevel: 2, maxHeadingLevel: 6 },
      credits: true,
      title: "TLSDZ",
      editLink: {
        baseUrl:
          "https://github.com/glennhenry/DeadZone-Documentation/edit/main/",
      },
      social: [
        {
          icon: "github",
          label: "GitHub",
          href: "https://github.com/SulivanM/DeadZone-Private-Server",
        },
        {
          icon: "github",
          label: "GitHub",
          href: "https://github.com/glennhenry/DeadZone-Documentation",
        },
      ],
      sidebar: [
        { label: "Main", slug: "main" },
        { label: "Dictionary", slug: "dictionary" },
        {
          label: "PlayerIO",
          items: [
            { label: "PlayerIO", slug: "playerio/playerio" },
            { label: "PlayerIOConnector", slug: "playerio/playerioconnector" },
            { label: "GameFS", slug: "playerio/gamefs" },
          ],
        },
      ],
    }),
  ],
});
