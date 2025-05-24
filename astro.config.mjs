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
        { label: "Sequential Flow", slug: "sequential_flow" },
        { label: "Dictionary", slug: "dictionary" },
        {
          label: "Common",
          collapsed: true,
          items: [
            {
              label: "PlayerIO",
              collapsed: true,
              items: [
                { label: "PlayerIO", slug: "common/playerio/playerio" },
                { label: "GameFS", slug: "common/playerio/gamefs" },
                { label: "HTTPChannel", slug: "common/playerio/httpchannel" },
                {
                  label: "PublishingNetwork",
                  slug: "common/playerio/publishingnetwork",
                },
              ],
            },
            {
              label: "TLSapp",
              collapsed: true,
              items: [
                {
                  label: "PlayerIOConnector",
                  slug: "common/tlsapp/playerioconnector",
                },
              ],
            },
          ],
        },
        {
          label: "Preloader",
          collapsed: true,
          items: [{ label: "Main", slug: "preloader/main" }],
        },
        {
          label: "Core",
          collapsed: true,
          items: [{ label: "Main", slug: "core/main" }],
        },
      ],
    }),
  ],
});
