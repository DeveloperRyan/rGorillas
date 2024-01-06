package org.ryno.tree.leaf.combat

import org.powbot.api.Condition
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Magic
import org.powbot.api.script.tree.Leaf
import org.ryno.Constants
import org.ryno.Script
import org.ryno.utils.InventoryUtils

class AlchItem(script: Script) : Leaf<Script>(script, "Alch Item") {
    override fun execute() {
        InventoryUtils.handleFullInventory()

        val item = Inventory.stream().id(*Constants.ALCH_WHITELIST).first()

        if (item.valid() && Magic.Spell.HIGH_ALCHEMY.cast("Cast")) {
            Condition.wait( { Game.tab() == Game.Tab.INVENTORY }, 300, 2 )

            if (Game.tab() == Game.Tab.INVENTORY && item.interact("Cast")) {
                Condition.wait({ !item.valid() }, 300, 4)
            }
        }
    }
}