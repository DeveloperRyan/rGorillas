package org.ryno.tree.branch

import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.SimpleLeaf
import org.powbot.api.script.tree.TreeComponent
import org.powbot.dax.api.models.RunescapeBank
import org.ryno.Constants
import org.ryno.Script
import org.ryno.tree.leaf.EatFood
import org.ryno.tree.leaf.banking.*
import org.ryno.utils.InventoryUtils
import org.ryno.utils.Utils

class ShouldWalkToBank(script: Script) : Branch<Script>(script, "Should Walk to Bank") {
    override val failedComponent: TreeComponent<Script> = ShouldOpenBank(script)
    override val successComponent: TreeComponent<Script> = WalkToBank(script)

    override fun validate(): Boolean {
        return Bank.nearest().distanceTo(Players.local()) > 4
    }
}

class ShouldOpenBank(script: Script) : Branch<Script>(script, "Should Open Bank") {
    override val failedComponent: TreeComponent<Script> = ShouldDepositLoot(script)
    override val successComponent: TreeComponent<Script> = OpenBank(script)

    override fun validate(): Boolean {
        return !Bank.opened()
    }
}

class ShouldDepositLoot(script: Script) : Branch<Script>(script, "Should Deposit Loot") {
    override val failedComponent: TreeComponent<Script> = ShouldDepositPotion(script)
    override val successComponent: TreeComponent<Script> = DepositLoot(script)

    override fun validate(): Boolean {
        return Inventory.stream().id(*Constants.BANKING_WHITELIST).isNotEmpty()
    }
}

class ShouldDepositPotion(script: Script) : Branch<Script>(script, "Should Deposit Potion") {
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawPotion(script)
    override val successComponent: TreeComponent<Script> = DepositPotion(script)

    override fun validate(): Boolean {
        return script.configuration.potions.any { it.shouldDeposit() }
    }
}

class ShouldWithdrawPotion(script: Script) : Branch<Script>(script, "Should Withdraw Potion") {
    override val failedComponent: TreeComponent<Script> = ShouldWithdrawFood(script)
    override val successComponent: TreeComponent<Script> = WithdrawPotion(script)

    override fun validate(): Boolean {
        return script.configuration.potions.any { it.shouldWithdraw() }
    }
}

class ShouldWithdrawFood(script: Script) : Branch<Script>(script, "Should Withdraw Food") {
    override val failedComponent: TreeComponent<Script> = ShouldDrinkPotionAtBank(script)
    override val successComponent: TreeComponent<Script> = WithdrawFood(script)

    override fun validate(): Boolean {
        return Inventory.emptySlotCount() > 0
    }
}

class ShouldDrinkPotionAtBank(script: Script) : Branch<Script>(script, "Should Drink Prayer Potion at Bank") {
    override val failedComponent: TreeComponent<Script> = ShouldEatFoodAtBank(script)
    override val successComponent: TreeComponent<Script> = DrinkPrayerPotion(script)

    override fun validate(): Boolean {
        return Skills.realLevel(Skill.Prayer) - Skills.level(Skill.Prayer) >= 26
    }
}

class ShouldEatFoodAtBank(script: Script) : Branch<Script>(script, "Should Eat Food at Bank") {
    override val failedComponent: TreeComponent<Script> = CloseBank(script)
    override val successComponent: TreeComponent<Script> = EatFood(script)

    override fun validate(): Boolean {
        return Players.local().healthPercent() < 90
    }
}
