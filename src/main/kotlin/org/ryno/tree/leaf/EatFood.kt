package org.ryno.tree.leaf

import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.State

class EatFood(script: Script) : Leaf<Script>(script, "Eat Food") {
    override fun execute() {
        val food = Inventory.stream().action("Eat").first()
        val currentHealth = Players.local().healthPercent()

        if (food.valid() && food.interact("Eat")) {
            Condition.wait({ Players.local().healthPercent() > currentHealth }, 300, 5)
            State.foodDelay = 3 // We use a delay to prevent trying to eat the next tick which will fail
        }
    }
}