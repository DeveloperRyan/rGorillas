package org.ryno.utils

import org.powbot.api.Condition
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import org.powbot.api.rt4.Players
import org.ryno.Constants

object InventoryUtils {
    fun getInventoryItem(vararg itemId: Int): Item {
        return Inventory.stream().id(*itemId).first()
    }

    fun getInventoryItem(name: String): Item {
        return Inventory.stream().name(name).first()
    }

    fun getInventoryItemCount(vararg itemId: Int): Int {
        return Inventory.stream().id(*itemId).count().toInt()
    }

    fun getInventoryItemCount(name: String): Int {
        return Inventory.stream().name(name).count().toInt()
    }

    fun hasFood(): Boolean {
        return Inventory.stream().action("Eat").isNotEmpty()
    }

    fun hasPrayerPotion(): Boolean {
        val hasPrayerPotion = Inventory.stream().nameContains(Constants.PRAYER_POTION_NAME).isNotEmpty()
        val hasSuperRestore = Inventory.stream().nameContains(Constants.SUPER_RESTORE_POTION_NAME).isNotEmpty()

        return hasPrayerPotion || hasSuperRestore
    }

    fun handleFullInventory() {
        if (Inventory.isFull() && hasFood()) {
            Utils.utilsLogger.info("Inventory is full, eating food to make space")
            val food = Inventory.stream().action("Eat").first()

            if (food.valid() && food.interact("Eat")) {
                Condition.wait({ !Inventory.isFull() }, 300, 4)
            }
        }
    }

    fun hasSeedPod(): Boolean {
        return Inventory.stream().name(Constants.ROYAL_SEED_POD_NAME).isNotEmpty()
    }
}