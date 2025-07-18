package dev.deadzone.core.model.game.data

import kotlinx.serialization.Serializable
import dev.deadzone.core.model.game.data.injury.InjuryCause
import dev.deadzone.core.model.game.data.WeaponClass

@Serializable
data class Weapon(
    val attachments: List<String>,
    val burstFire: Boolean,
    val injuryCause: InjuryCause,
    val weaponClass: WeaponClass,
    val animType: String,
    val reloadAnim: String,
    val swingAnims: List<String>,
    val playSwingExertionSound: Boolean = true,
    val flags: UInt = 0,
    val weaponType: UInt = 0,
    val ammoType: UInt = 0,
    val survivorClasses: List<String> = listOf()
)
