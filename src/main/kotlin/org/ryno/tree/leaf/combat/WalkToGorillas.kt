package org.ryno.tree.leaf.combat

import org.powbot.api.rt4.Movement
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager
import org.ryno.Constants
import org.ryno.Script

class WalkToGorillas(script: Script) : Leaf<Script>(script, "Walk to Gorillas") {
    override fun execute() {
        script.localLogger.info("Walking to Gorillas")
        Movement.builder(Constants.GORILLAS_DESTINATION_TILE).setRunMin(2).setRunMax(7).setAutoRun(true)
            .setWalkUntil { ScriptManager.isStopping() }.move()
    }
}