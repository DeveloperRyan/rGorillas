package org.ryno.models

import org.powbot.api.Notifications
import org.powbot.api.rt4.Magic
import org.powbot.mobile.script.ScriptManager
import org.ryno.Constants
import org.ryno.Constants.RangePotionType
import org.ryno.Constants.CombatPotionType
import org.ryno.State
import org.ryno.utils.InventoryUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val configurationLogger: Logger = LoggerFactory.getLogger("Configuration")
class Configuration(
    var meleeSetup: IntArray = intArrayOf(),
    var rangedSetup: IntArray = intArrayOf(),
    var foodName: String = "Shark",
    var shouldAlch: Boolean = true,
    var eatAtPercent: Int = 50,
    var combatPotionType: CombatPotionType = CombatPotionType.NONE,
    var combatPotionCount: Int = 0,
    var rangePotionType: RangePotionType = RangePotionType.NONE,
    var rangePotionCount: Int = 0,
    prayerPotionType: Constants.PrayerPotionType = Constants.PrayerPotionType.PRAYER,
    var prayerPotionCount: Int = 0,
) {
    val potions = mutableListOf<Potion>().apply {
        if (combatPotionType == CombatPotionType.SUPER_SET) {
            add(Potion.CombatPotion(CombatPotionType.SUPER_ATTACK, combatPotionCount))
            add(Potion.CombatPotion(CombatPotionType.SUPER_STRENGTH, combatPotionCount))
        } else {
            add(Potion.CombatPotion(combatPotionType, combatPotionCount))
        }
        add(Potion.RangingPotion(rangePotionType, rangePotionCount))
        add(Potion.PrayerPotion(prayerPotionType, prayerPotionCount))
    }

    override fun toString(): String {
        return "Configuration(meleeSetup=$meleeSetup, rangedSetup=$rangedSetup, foodName='$foodName', shouldAlch='$shouldAlch', combatPotionType=$combatPotionType, combatPotionCount=$combatPotionCount, rangePotionType=$rangePotionType, rangePotionCount=$rangePotionCount, prayerPotionCount=$prayerPotionCount, potions=$potions)"
    }
}

object ConfigurationFactory {
    fun create(
        meleeSetup: IntArray,
        rangedSetup: IntArray,
        foodName: String,
        shouldAlch: Boolean,
        eatAtPercent: Int,
        combatPotionTypeString: String,
        combatPotionCount: Int,
        rangePotionTypeString: String,
        rangePotionCount: Int,
        prayerPotionTypeString: String,
        prayerPotionCount: Int
    ): Configuration {
        if (shouldAlch && !Magic.Spell.HIGH_ALCHEMY.canCast()) {
            Notifications.showNotification("Please withdraw alch runes or disable 'Should Alch'.")
            configurationLogger.error("Alch Enabled, but can't cast. Stopping script.")
            ScriptManager.stop()
        }

        val combatPotionType = when (combatPotionTypeString) {
            "Super Combat Potion" -> CombatPotionType.SUPER_COMBAT
            "Divine Super Combat Potion" -> CombatPotionType.DIVINE_SUPER_COMBAT
            "Super Set" -> CombatPotionType.SUPER_SET
            else -> CombatPotionType.NONE
        }

        val rangePotionType = when (rangePotionTypeString) {
            "Ranging Potion" -> RangePotionType.RANGING
            "Divine Ranging Potion" -> RangePotionType.DIVINE_RANGING
            else -> RangePotionType.NONE
        }

        val prayerPotionType = when (prayerPotionTypeString) {
            "Super Restore Potion" -> Constants.PrayerPotionType.SUPER_RESTORE
            else -> Constants.PrayerPotionType.PRAYER
        }

        if (meleeSetup.any { it in Constants.CHARGED_ITEMS } || rangedSetup.any { it in Constants.CHARGED_ITEMS }) {
            configurationLogger.error("Charged items in gear setup. Enabling hasChargedItems.")
            State.hasChargedItems = true
        }

        if (meleeSetup.isEmpty() || rangedSetup.isEmpty()) {
            configurationLogger.error("Melee or Ranged setup is empty. Stopping script.")
            Notifications.showNotification("Melee or Ranged setup is empty. Stopping script.")
            ScriptManager.stop()
        }

        if (!InventoryUtils.hasSeedPod()) {
            configurationLogger.error("No Royal seed pod in inventory. Stopping script.")
            Notifications.showNotification("Please start the script with a Royal seed pod in your inventory.")
            ScriptManager.stop()
        }

        return Configuration(
            meleeSetup = meleeSetup,
            rangedSetup = rangedSetup,
            foodName = foodName,
            shouldAlch = shouldAlch,
            eatAtPercent = eatAtPercent,
            combatPotionType = combatPotionType,
            combatPotionCount = if (combatPotionType == CombatPotionType.NONE) 0 else combatPotionCount,
            rangePotionType = rangePotionType,
            rangePotionCount = if (rangePotionType == RangePotionType.NONE) 0 else rangePotionCount,
            prayerPotionType = prayerPotionType,
            prayerPotionCount = prayerPotionCount
        )
    }
}