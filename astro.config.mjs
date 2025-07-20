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
      lastUpdated: true,
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
        { label: "Glossary", slug: "glossary" },
        { label: "Architecture", slug: "architecture" },
        { label: "API Server", slug: "api-server" },
        { label: "preloader/Main.as", slug: "preloader-main" },
        { label: "core/Main.as", slug: "core-main" },
        { label: "core/Game.as", slug: "game" },
        {
          label: "playerio",
          collapsed: true,
          items: [
            {
              label: "utils",
              collapsed: true,
              items: [
                {
                  label: "BinarySerializer",
                  slug: "playerio/utils/binaryserializer",
                },
                { label: "Converter", slug: "playerio/utils/converter" },
                { label: "HTTPChannel", slug: "playerio/utils/httpchannel" },
              ],
            },
            {
              label: "generated.messages",
              collapsed: true,
              items: [
                { label: "Overview", slug: "playerio/generated/messages" },
              ],
            },
            { label: "BigDB", slug: "playerio/bigdb" },
            { label: "Connection", slug: "playerio/connection" },
            { label: "DatabaseObject", slug: "playerio/databaseobject" },
            { label: "GameFS", slug: "playerio/gamefs" },
            { label: "Message", slug: "playerio/message" },
            { label: "Multiplayer", slug: "playerio/multiplayer" },
            { label: "PlayerIO", slug: "playerio/playerio" },
            { label: "PlayerIOError", slug: "playerio/playerioerror" },
            {
              label: "PublishingNetwork",
              slug: "playerio/publishingnetwork",
            },
          ],
        },
        {
          label: "thelaststand.app",
          collapsed: true,
          items: [
            {
              label: "data",
              collapsed: true,
              items: [
                {
                  label: "CostTable",
                  slug: "thelaststand/app/data/costtable",
                },
                {
                  label: "PlayerData",
                  slug: "thelaststand/app/data/playerdata",
                },
              ],
            },
            {
              label: "game",
              collapsed: true,
              items: [
                {
                  label: "data",
                  collapsed: true,
                  items: [
                    {
                      label: "injury",
                      collapsed: true,
                      items: [
                        {
                          label: "Injury",
                          slug: "thelaststand/app/game/data/injury/injury",
                        },
                        {
                          label: "InjuryList",
                          slug: "thelaststand/app/game/data/injury/injurylist",
                        },
                      ],
                    },
                    {
                      label: "AttireData",
                      slug: "thelaststand/app/game/data/attiredata",
                    },
                    {
                      label: "Attributes",
                      slug: "thelaststand/app/game/data/attributes",
                    },
                    {
                      label: "Gender",
                      slug: "thelaststand/app/game/data/gender",
                    },
                    {
                      label: "HumanAppearance",
                      slug: "thelaststand/app/game/data/humanappearance",
                    },
                    {
                      label: "Item",
                      slug: "thelaststand/app/game/data/item",
                    },
                    {
                      label: "Morale",
                      slug: "thelaststand/app/game/data/morale",
                    },
                    {
                      label: "Survivor",
                      slug: "thelaststand/app/game/data/survivor",
                    },
                    {
                      label: "SurvivorAppearance",
                      slug: "thelaststand/app/game/data/survivorappearance",
                    },
                    {
                      label: "SurvivorClass",
                      slug: "thelaststand/app/game/data/survivorclass",
                    },
                    {
                      label: "SurvivorLoadout",
                      slug: "thelaststand/app/game/data/survivorloadout",
                    },
                    {
                      label: "TimerData",
                      slug: "thelaststand/app/game/data/timerdata",
                    },
                    {
                      label: "WeaponClass",
                      slug: "thelaststand/app/game/data/weaponclass",
                    },
                    {
                      label: "WeaponData",
                      slug: "thelaststand/app/game/data/weapondata",
                    },
                    {
                      label: "WeaponType",
                      slug: "thelaststand/app/game/data/weapontype",
                    },
                  ],
                },
              ],
            },
            {
              label: "network",
              collapsed: true,
              items: [
                {
                  label: "Network",
                  slug: "thelaststand/app/network/network",
                },
                {
                  label: "NetworkMessage",
                  slug: "thelaststand/app/network/networkmessage",
                },
                {
                  label: "PlayerIOConnector",
                  slug: "thelaststand/app/network/playerioconnector",
                },
              ],
            },
          ],
        },
      ],
    }),
  ],
});
