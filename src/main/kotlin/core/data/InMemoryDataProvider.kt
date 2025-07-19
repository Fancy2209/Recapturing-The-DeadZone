package dev.deadzone.core.data

/**
 * In memory data provider
 *
 * Used to replace retrieving data directly from DB
 */
object InMemoryDataProvider : DataProvider {
    private val mockData = mapOf(
        "login_state.json" to loginState.trimIndent(),
        "cost_table.json" to costTable.trimIndent(),
        "srv_table.json" to SrvTable.trimIndent()
    )

    override fun loadRawJson(path: String): String {
        return mockData[path] ?: error("Missing mock data for: $path")
    }
}

const val SrvTable = """
{
  "fighter": {
    "id": "fighter",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 1,
      "combatProjectile": 1,
      "combatMelee": 1,
      "combatImprovised": 1,
      "movement": 1,
      "scavenge": 1,
      "healing": 0,
      "trapSpotting": 0,
      "trapDisarming": 0
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "medic": {
    "id": "medic",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 1.0,
      "combatProjectile": 0.6,
      "combatMelee": 0.7,
      "combatImprovised": 0.5,
      "movement": 1.1,
      "scavenge": 0.9,
      "healing": 1.5,
      "trapSpotting": 0.5,
      "trapDisarming": 0.5
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "scavenger": {
    "id": "scavenger",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 0.9,
      "combatProjectile": 0.5,
      "combatMelee": 0.6,
      "combatImprovised": 0.7,
      "movement": 1.4,
      "scavenge": 1.6,
      "healing": 0.3,
      "trapSpotting": 0.8,
      "trapDisarming": 0.5
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "engineer": {
    "id": "engineer",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 1.0,
      "combatProjectile": 0.6,
      "combatMelee": 0.5,
      "combatImprovised": 0.9,
      "movement": 1.0,
      "scavenge": 1.0,
      "healing": 0.2,
      "trapSpotting": 1.2,
      "trapDisarming": 1.5
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "recon": {
    "id": "recon",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 1.0,
      "combatProjectile": 1.4,
      "combatMelee": 0.7,
      "combatImprovised": 0.6,
      "movement": 1.5,
      "scavenge": 1.2,
      "healing": 0.1,
      "trapSpotting": 1.0,
      "trapDisarming": 0.8
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "player": {
    "id": "player",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 1.0,
      "combatProjectile": 1.0,
      "combatMelee": 1.0,
      "combatImprovised": 1.0,
      "movement": 1.0,
      "scavenge": 1.0,
      "healing": 1.0,
      "trapSpotting": 1.0,
      "trapDisarming": 1.0
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  },
  "unassigned": {
    "id": "unassigned",
    "maleUpper": "fighter_upper_m",
    "maleLower": "fighter_lower_m",
    "femaleUpper": "fighter_upper_f",
    "femaleLower": "fighter_lower_f",
    "baseAttributes": {
      "health": 0,
      "combatProjectile": 0,
      "combatMelee": 0,
      "combatImprovised": 0,
      "movement": 0,
      "scavenge": 0,
      "healing": 0,
      "trapSpotting": 0,
      "trapDisarming": 0
    },
    "levelAttributes": {
      "health": 0.1,
      "combatProjectile": 0.05,
      "combatMelee": 0.05,
      "combatImprovised": 0.05,
      "movement": 0.03,
      "scavenge": 0.03,
      "healing": 0.02,
      "trapSpotting": 0.01,
      "trapDisarming": 0.01
    },
    "weapons": {
      "classes": [
        "rifle",
        "melee"
      ],
      "types": [
        "primary",
        "secondary"
      ]
    },
    "hideHair": false
  }
}
    
"""

const val costTable = """
{
  "buildings": {
    "barricade": {
      "wood": 10,
      "metal": 5
    },
    "turret": {
      "wood": 50,
      "metal": 25
    }
  },
  "upgrades": {
    "storage": {
      "cost": 1000
    },
    "speed": {
      "cost": 500
    }
  }
}
"""

const val loginState = """
{
  "settings": {
    "volume": 0.8,
    "language": "en"
  },
  "news": {},
  "sales": [],
  "allianceWinnings": {},
  "recentPVPList": [
    {
      "opponent": "Player123",
      "result": "win"
    },
    {
      "opponent": "Player456",
      "result": "loss"
    }
  ],
  "invsize": 8,
  "upgrades": "",
  "allianceId": null,
  "allianceTag": null,
  "longSession": true,
  "leveledUp": false,
  "promos": [],
  "promoSale": null,
  "dealItem": null,
  "leaderResets": 0,
  "unequipItemBinds": null,
  "globalStats": {
    "idList": []
  },
  "inventory": [],
  "neighborHistory": {},
  "zombieAttack": false,
  "zombieAttackLogins": 0,
  "offersEnabled": false,
  "lastLogout": null,
  "prevLogin": null
}
"""
