package org.ryno.tree.leaf.combat

import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.ryno.Script

class DropVial(script: Script) : Leaf<Script>(script, "Drop Vial") {
    override fun execute() {
        val vial = Inventory.stream().name("Vial").first()
        val emptySlots = Inventory.emptySlotCount()
        if (vial.valid() && vial.interact("Drop")) {
            Condition.wait({ Inventory.emptySlotCount() < emptySlots }, 300, 5)
        }
    }
}