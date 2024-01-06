package org.ryno.tree.leaf

import org.powbot.api.Condition
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.State

class DrinkPotion(script: Script) : Leaf<Script>(script, "Drink Potion") {

    override fun execute() {
        // Get the first potion that we should drink
        val potionType = script.configuration.potions.first { it.shouldDrink() }
        val potionToDrink = potionType.getPotionToDrink()

        if (potionToDrink.valid() && potionToDrink.interact("Drink")) {
            Condition.wait({ !potionType.shouldDrink() }, 300, 5)
            State.potionDelay = 3
        }
    }
}