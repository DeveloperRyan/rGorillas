package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.ryno.Script

class WithdrawFood(script: Script) : Leaf<Script>(script, "Withdraw Food") {
    override fun execute() {
        script.localLogger.info("Withdrawing ${script.configuration.foodName}")

        if (Bank.withdraw(script.configuration.foodName, Bank.Amount.ALL)) {
            Condition.wait({ Inventory.isFull() }, 300, 4)
            script.localLogger.info("Withdrew ${script.configuration.foodName}")
        } else {
            script.localLogger.info("Failed to withdraw ${script.configuration.foodName}")
        }
    }
}