package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.ryno.Constants
import org.ryno.Script

class DepositLoot(script: Script) : Leaf<Script>(script, "Deposit Loot") {
    override fun execute() {
        script.localLogger.info("Depositing Loot")

        Inventory.stream().filtered {
            it.id in Constants.LOOT_WHITELIST && it.name() != script.configuration.foodName && it.name() != "Prayer potion(3)"
        }.forEach {
            if (Bank.deposit(it.id, Bank.Amount.ALL)) {
                Condition.wait({ !it.valid() }, 300, 4)
            }
        }
    }
}