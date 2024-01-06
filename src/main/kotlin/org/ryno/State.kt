package org.ryno

import org.powbot.api.Tile
import org.powbot.api.rt4.Npc
import org.powbot.api.rt4.Prayer

object State {
    var hasChargedItems: Boolean = false
    var gorillaAttackStyle: Constants.AttackStyles = Constants.AttackStyles.NONE
    var playerAttackStyle: Constants.AttackStyles = Constants.AttackStyles.NONE
    var gorillaPrayer: Prayer.Effect? = null
    var attackCounter = 0
    var targetGorilla: Npc? = null
    var roarDelay = 0 // Roar Delay is used to prevent repeatedly changing prayers since text stays for a few ticks
    var activeGearSetup: IntArray = intArrayOf()

    var killCount = 0
    var shouldLoot = false
    var lootCounter = 0
    var lootTile: Tile? = null

    // Eating / drinking have a delay for a repeated action, these delays are used to prevent trying to eat when we cannot
    var foodDelay = 0
    var potionDelay = 0

    var shouldBreak: Boolean = false
}