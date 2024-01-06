package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.utils.InventoryUtils

class DrinkPrayerPotion(script: Script) : Leaf<Script>(script, "Drink Prayer Potion") {
    override fun execute() {
        val potion = InventoryUtils.getInventoryItem("Prayer potion([1-4])")

        if (potion.valid() && potion.interact("Drink")) {
            Condition.wait({ !potion.valid() }, 300, 5)
            script.localLogger.info("Drank prayer potion in bank")
        }
    }
}