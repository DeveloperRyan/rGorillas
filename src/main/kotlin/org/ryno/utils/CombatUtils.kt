package org.ryno.utils

import org.powbot.api.rt4.Npc
import org.powbot.api.rt4.Npcs
import org.powbot.api.rt4.Prayer
import org.ryno.Constants
import org.ryno.State

object CombatUtils {
    fun getInteractingGorilla(): Npc {
        return Npcs.stream().interactingWithMe().nearest().first()
    }

    fun getActivePrayer(overheadId: Int): Prayer.Effect? {
        return when (overheadId) {
            0 -> Prayer.Effect.PROTECT_FROM_MELEE
            1 -> Prayer.Effect.PROTECT_FROM_MISSILES
            2 -> Prayer.Effect.PROTECT_FROM_MAGIC
            else -> null
        }
    }

    fun getCorrectPrayerForAttackStyle(attackStyle: Constants.AttackStyles): Prayer.Effect? {
        return when (attackStyle) {
            Constants.AttackStyles.MAGIC -> Prayer.Effect.PROTECT_FROM_MAGIC
            Constants.AttackStyles.RANGED -> Prayer.Effect.PROTECT_FROM_MISSILES
            Constants.AttackStyles.MELEE -> Prayer.Effect.PROTECT_FROM_MELEE
            else -> null
        }
    }

    fun getCorrectAttackStyleForPrayer(prayer: Prayer.Effect?): Constants.AttackStyles {
        return when (prayer) {
            Prayer.Effect.PROTECT_FROM_MAGIC -> Constants.AttackStyles.MELEE
            Prayer.Effect.PROTECT_FROM_MISSILES -> Constants.AttackStyles.MELEE
            Prayer.Effect.PROTECT_FROM_MELEE -> Constants.AttackStyles.RANGED
            else -> State.playerAttackStyle
        }
    }
}