package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.Notifications
import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.Prayer
import org.powbot.api.script.tree.Leaf
import org.powbot.dax.api.models.RunescapeBank
import org.ryno.Script
import org.ryno.utils.Utils

class WalkToBank(script: Script) : Leaf<Script>(script, "Walk to Bank") {
    override fun execute() {
        if (!Utils.handleGrandTreeTeleport()) {
            script.localLogger.info("Failed to handle Grand Tree teleport while Banking")
            Notifications.showNotification("Failed to teleport to bank; walking back instead")
            
            Movement.moveToBank(RunescapeBank.GNOME_TREE_BANK_SOUTH)

            return
        }

        if (Prayer.prayersActive()) {
            Prayer.activePrayers().forEach {
                script.localLogger.info("Disabling prayer: ${it.name}")
                Prayer.prayer(it, false)
                Condition.wait({ !Prayer.prayerActive(it) }, 300, 4)
            }
        }

        Movement.moveToBank(RunescapeBank.GNOME_TREE_BANK_SOUTH)
    }
}