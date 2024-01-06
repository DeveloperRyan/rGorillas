package org.ryno.models

import org.powbot.api.rt4.Item
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.ryno.Constants
import org.ryno.Constants.ATTACK_POTIONS
import org.ryno.Constants.COMBAT_POTIONS
import org.ryno.Constants.DIVINE_COMBAT_POTIONS
import org.ryno.Constants.DIVINE_RANGING_POTIONS
import org.ryno.Constants.PRAYER_POTIONS
import org.ryno.Constants.RANGING_POTIONS
import org.ryno.Constants.STRENGTH_POTIONS
import org.ryno.Constants.SUPER_RESTORE_POTIONS
import org.ryno.utils.BankUtils.getCorrectPotionFromBank
import org.ryno.utils.InventoryUtils.getInventoryItem
import org.ryno.utils.InventoryUtils.getInventoryItemCount

private const val SUPER_ATTACK_BASE_NAME = "Super attack"
private const val SUPER_STRENGTH_BASE_NAME = "Super strength"
private const val SUPER_COMBAT_BASE_NAME = "Super combat potion"
private const val DIVINE_SUPER_COMBAT_BASE_NAME = "Divine super combat potion"
private const val RANGING_BASE_NAME = "Ranging potion"
private const val DIVINE_RANGING_BASE_NAME = "Divine ranging potion"
private const val PRAYER_BASE_NAME = "Prayer potion"
private const val SUPER_RESTORE_BASE_NAME = "Super restore"

sealed class Potion(open val count: Int) {
    abstract fun shouldDrink(): Boolean
    abstract fun shouldWithdraw(): Boolean
    abstract fun shouldDeposit(): Boolean
    abstract fun getPotionToWithdraw(): Item
    abstract fun getPotionToDeposit(): Item
    abstract fun getPotionToDrink(): Item


    // NOTE: All potions are in an int array of size 4; 0 = (1) dose, 1 = (2) dose, 2 = (3) dose, 3 = (4) dose
    // This makes it a little cleaned to pass a slice of the array to get the correct potion
    // Rather than passing individual potion IDs
    data class CombatPotion(val type: Constants.CombatPotionType, override val count: Int) : Potion(count) {
        override fun shouldDrink() = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> Skills.level(Skill.Attack) - Skills.realLevel(Skill.Attack) < 2
            Constants.CombatPotionType.SUPER_STRENGTH -> Skills.level(Skill.Strength) - Skills.realLevel(Skill.Strength) < 2
            Constants.CombatPotionType.SUPER_COMBAT -> Skills.level(Skill.Attack) - Skills.realLevel(Skill.Attack) < 2
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> Skills.level(Skill.Attack) - Skills.realLevel(Skill.Attack) < 2
            else -> false
        }

        override fun shouldWithdraw() = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> getInventoryItemCount(*ATTACK_POTIONS.sliceArray(2..3)) < count
            Constants.CombatPotionType.SUPER_STRENGTH -> getInventoryItemCount(*STRENGTH_POTIONS.sliceArray(2..3)) < count
            Constants.CombatPotionType.SUPER_COMBAT -> getInventoryItemCount(*COMBAT_POTIONS.sliceArray(2..3)) < count
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> getInventoryItemCount(*DIVINE_COMBAT_POTIONS.sliceArray(2..3)) < count
            else -> false
        }

        override fun shouldDeposit() = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> getInventoryItemCount(*ATTACK_POTIONS.sliceArray(0..1)) > 0
            Constants.CombatPotionType.SUPER_STRENGTH -> getInventoryItemCount(*STRENGTH_POTIONS.sliceArray(0..1)) > 0
            Constants.CombatPotionType.SUPER_COMBAT -> getInventoryItemCount(*COMBAT_POTIONS.sliceArray(0..1)) > 0
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> getInventoryItemCount(*DIVINE_COMBAT_POTIONS.sliceArray(0..1)) > 0
            else -> false
        }

        override fun getPotionToWithdraw(): Item = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> getCorrectPotionFromBank(SUPER_ATTACK_BASE_NAME)
            Constants.CombatPotionType.SUPER_STRENGTH -> getCorrectPotionFromBank(SUPER_STRENGTH_BASE_NAME)
            Constants.CombatPotionType.SUPER_COMBAT -> getCorrectPotionFromBank(SUPER_COMBAT_BASE_NAME)
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> getCorrectPotionFromBank(DIVINE_SUPER_COMBAT_BASE_NAME)
            else -> Item.Nil
        }

        override fun getPotionToDeposit(): Item = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> getInventoryItem(*ATTACK_POTIONS.sliceArray(0..1))
            Constants.CombatPotionType.SUPER_STRENGTH -> getInventoryItem(*STRENGTH_POTIONS.sliceArray(0..1))
            Constants.CombatPotionType.SUPER_COMBAT -> getInventoryItem(*COMBAT_POTIONS.sliceArray(0..1))
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> getInventoryItem(*DIVINE_COMBAT_POTIONS.sliceArray(0..1))
            else -> Item.Nil
        }

        override fun getPotionToDrink(): Item = when (type) {
            Constants.CombatPotionType.SUPER_ATTACK -> getInventoryItem(*ATTACK_POTIONS.sliceArray(0..3))
            Constants.CombatPotionType.SUPER_STRENGTH -> getInventoryItem(*STRENGTH_POTIONS.sliceArray(0..3))
            Constants.CombatPotionType.SUPER_COMBAT -> getInventoryItem(*COMBAT_POTIONS.sliceArray(0..3))
            Constants.CombatPotionType.DIVINE_SUPER_COMBAT -> getInventoryItem(*DIVINE_COMBAT_POTIONS.sliceArray(0..3))
            else -> Item.Nil
        }
    }

    data class RangingPotion(val type: Constants.RangePotionType, override val count: Int) : Potion(count) {
        override fun shouldDrink() = when (type) {
            Constants.RangePotionType.NONE -> false
            else -> Skills.level(Skill.Ranged) - Skills.realLevel(Skill.Ranged) < 2
        }

        override fun shouldWithdraw() = when (type) {
            Constants.RangePotionType.RANGING -> getInventoryItemCount(*RANGING_POTIONS.sliceArray(2..3)) < count
            Constants.RangePotionType.DIVINE_RANGING -> getInventoryItemCount(*DIVINE_RANGING_POTIONS.sliceArray(2..3)) < count
            else -> false
        }

        override fun shouldDeposit() = when (type) {
            Constants.RangePotionType.RANGING -> getInventoryItemCount(*RANGING_POTIONS.sliceArray(0..1)) > 0
            Constants.RangePotionType.DIVINE_RANGING -> getInventoryItemCount(*DIVINE_RANGING_POTIONS.sliceArray(0..1)) > 0
            else -> false
        }

        override fun getPotionToWithdraw(): Item = when (type) {
            Constants.RangePotionType.RANGING -> getCorrectPotionFromBank(RANGING_BASE_NAME)
            Constants.RangePotionType.DIVINE_RANGING -> getCorrectPotionFromBank(DIVINE_RANGING_BASE_NAME)
            else -> Item.Nil
        }

        override fun getPotionToDeposit(): Item = when (type) {
            Constants.RangePotionType.RANGING -> getInventoryItem(*RANGING_POTIONS.sliceArray(0..1))
            Constants.RangePotionType.DIVINE_RANGING -> getInventoryItem(
                *DIVINE_RANGING_POTIONS.sliceArray(0..1)
            )

            else -> Item.Nil
        }

        override fun getPotionToDrink(): Item = when (type) {
            Constants.RangePotionType.RANGING -> getInventoryItem(*RANGING_POTIONS.sliceArray(0..3))
            Constants.RangePotionType.DIVINE_RANGING -> getInventoryItem(*DIVINE_RANGING_POTIONS.sliceArray(0..3))
            else -> Item.Nil
        }
    }

    data class PrayerPotion(val type: Constants.PrayerPotionType, override val count: Int) : Potion(count) {
        override fun shouldDrink() = when (type) {
            else -> Skills.realLevel(Skill.Prayer) - Skills.level(Skill.Prayer) >= 26
        }

        override fun shouldWithdraw() = when (type) {
            Constants.PrayerPotionType.PRAYER -> getInventoryItemCount(*PRAYER_POTIONS.sliceArray(2..3)) < count
            Constants.PrayerPotionType.SUPER_RESTORE -> getInventoryItemCount(*SUPER_RESTORE_POTIONS.sliceArray(2..3)) < count
        }

        override fun shouldDeposit() = when (type) {
            Constants.PrayerPotionType.PRAYER -> (getInventoryItemCount(*PRAYER_POTIONS.sliceArray(0..1)) > 0 ||
                    getInventoryItemCount(*PRAYER_POTIONS.sliceArray(2..3)) > count)

            Constants.PrayerPotionType.SUPER_RESTORE -> (getInventoryItemCount(*SUPER_RESTORE_POTIONS.sliceArray(0..1)) > 0 ||
                    getInventoryItemCount(*SUPER_RESTORE_POTIONS.sliceArray(2..3)) > count)
        }

        override fun getPotionToWithdraw(): Item = when (type) {
            Constants.PrayerPotionType.PRAYER -> getCorrectPotionFromBank(PRAYER_BASE_NAME)
            Constants.PrayerPotionType.SUPER_RESTORE -> getCorrectPotionFromBank(SUPER_RESTORE_BASE_NAME)
        }

        override fun getPotionToDeposit(): Item = when (type) {
            Constants.PrayerPotionType.PRAYER -> getInventoryItem(*PRAYER_POTIONS)
            Constants.PrayerPotionType.SUPER_RESTORE -> getInventoryItem(*SUPER_RESTORE_POTIONS)
        }

        override fun getPotionToDrink(): Item = when (type) {
            Constants.PrayerPotionType.PRAYER -> getInventoryItem(*PRAYER_POTIONS)
            Constants.PrayerPotionType.SUPER_RESTORE -> {
                if (getInventoryItemCount(*SUPER_RESTORE_POTIONS) == 0 && getInventoryItemCount(*PRAYER_POTIONS) > 0) {
                    getInventoryItem(*PRAYER_POTIONS)
                } else {
                    getInventoryItem(*SUPER_RESTORE_POTIONS)
                }
            }
        }
    }
}