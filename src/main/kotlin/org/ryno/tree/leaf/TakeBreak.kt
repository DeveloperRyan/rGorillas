package org.ryno.tree.leaf

import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.utils.Utils

class TakeBreak(script: Script): Leaf<Script>(script, "Take Break") {
    override fun execute() {
        if (!Utils.handleGrandTreeTeleport()) {
            script.localLogger.error("Failed to handle Grand Tree teleport for a break")

            Utils.walkToSafespot()
        }
        script.localLogger.info("Taking a break")
    }
}