package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.models.Potion
import org.ryno.utils.InventoryUtils

class WithdrawPotion(script: Script) : Leaf<Script>(script, "Withdraw Potion") {
    override fun execute() {
        val potionToWithdraw: Potion = script.configuration.potions.find {
            it.shouldWithdraw()
        } ?: return
        val itemToWithdraw = potionToWithdraw.getPotionToWithdraw()
        val currentCount = InventoryUtils.getInventoryItemCount(itemToWithdraw.id)
        val numToWithdraw = potionToWithdraw.count - currentCount

        if (Inventory.isFull() && InventoryUtils.hasFood()) {
            script.localLogger.info("Inventory is full; depositing some food to make space for potions")
            val foodToDeposit = Inventory.stream().name(script.configuration.foodName).first()
            if (foodToDeposit.valid() && Bank.deposit(foodToDeposit.id, numToWithdraw)) {
                Condition.wait({ !Inventory.isFull() }, 300, 4)
            }
        } else {
            script.localLogger.info("Inventory is full; but have no food to deposit")
        }

        if (!Inventory.isFull() && itemToWithdraw.valid() && Bank.withdraw(itemToWithdraw.id, numToWithdraw)) {
            Condition.wait({ InventoryUtils.getInventoryItemCount(itemToWithdraw.id) > currentCount }, 300, 4)

            script.localLogger.info("Withdrew $numToWithdraw ${itemToWithdraw.name()}")
        } else {
            script.localLogger.info("Failed to withdraw $numToWithdraw ${itemToWithdraw.name()}")
        }
    }
}