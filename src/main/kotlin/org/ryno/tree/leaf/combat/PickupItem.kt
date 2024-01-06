package org.ryno.tree.leaf.combat

import org.powbot.api.Condition
import org.powbot.api.rt4.Camera
import org.powbot.api.rt4.GroundItem
import org.powbot.api.rt4.GroundItems
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf
import org.ryno.Constants
import org.ryno.Script
import org.ryno.State
import org.ryno.utils.InventoryUtils
import org.ryno.utils.Utils

class PickupItem(script: Script) : Leaf<Script>(script, "Pickup Item") {
    override fun execute() {
        val item = getItemToPickup()

        if (Utils.getFallingBoulder().valid()) {
            script.localLogger.info("Boulder is falling, stopping looting")
            return
        }

        InventoryUtils.handleFullInventory()

        script.localLogger.info("Picking up item: ${item.name()}")

        if (item.valid()) {
            if (!item.inViewport()) {
                Camera.turnTo(item)
            }

            if (item.interact("Take")) {
                Condition.wait({ !item.valid() || Utils.getFallingBoulder().valid() }, 200, 5)
                script.localLogger.info("Picked up item: ${item.name()}")
            } else {
                script.localLogger.info("Failed to pick up item: ${item.name()}")
            }
        }
    }

    private fun getItemToPickup(): GroundItem {
        return if (Inventory.isFull()) {
            val lootItems = Constants.LOOT_WHITELIST.filter { it != Constants.SHARK_ID && it != Constants.PRAYER_POTION_3_ID }

            GroundItems.stream()
                .id(*lootItems.toIntArray())
                .within(State.lootTile!!, 3).nearest().first()
        } else {
            GroundItems.stream().id(*Constants.LOOT_WHITELIST).within(State.lootTile!!, 3).nearest().first()
        }
    }
}