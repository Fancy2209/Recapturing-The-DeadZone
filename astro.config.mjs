// @ts-check
import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";
import starlightThemeObsidian from "starlight-theme-obsidian";
import vercel from "@astrojs/vercel";
import rehypeExternalLinks from "rehype-external-links";

// https://astro.build/config
export default defineConfig({
  adapter: vercel({ imageService: true }),
  base: process.env.NODE_ENV === "production" ? "/" : "/",
  markdown: {
    rehypePlugins: [
      [
        rehypeExternalLinks,
        {
          content: { type: "text", value: " ↗" },
          target: "_blank",
          rel: ["noopener", "noreferrer"],
        },
      ],
    ],
  },
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
        {
          icon: "discord",
          label: "Discord",
          href: "https://discord.gg/Q5dTKrPmfq",
        },
      ],
      sidebar: [
        { label: "Intro", slug: "index" },
        { label: "Main", slug: "main" },
        { label: "Dictionary", slug: "dictionary" },
        {
          label: "PlayerIO",
          collapsed: true,
          items: [
            { label: "PlayerIO", slug: "playerio/playerio" },
            { label: "PlayerIOConnector", slug: "playerio/playerioconnector" },
            { label: "GameFS", slug: "playerio/gamefs" },
            { label: "HTTPChannel", slug: "playerio/httpchannel" },
            { label: "PublishingNetwork", slug: "playerio/publishingnetwork" },
          ],
        },
      ],
    }),
  ],
});
