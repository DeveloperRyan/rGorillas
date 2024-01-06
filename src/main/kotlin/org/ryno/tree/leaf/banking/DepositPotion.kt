package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.utils.InventoryUtils

class DepositPotion(script: Script) : Leaf<Script>(script, "Deposit Potion") {
    override fun execute() {
        val potion = script.configuration.potions.find { it.shouldDeposit() } ?: return
        val itemToDeposit = potion.getPotionToDeposit()
        val currentCount = InventoryUtils.getInventoryItemCount(itemToDeposit.id)

        if (itemToDeposit.valid() && Bank.deposit(itemToDeposit.id, Bank.Amount.ALL)) {
            script.localLogger.info("Depositing $currentCount ${itemToDeposit.name()}")
            Condition.wait({ InventoryUtils.getInventoryItemCount(itemToDeposit.id) < currentCount }, 300, 4)
        } else {
            script.localLogger.info("Failed to deposit $currentCount ${itemToDeposit.name()}")
        }
    }
}