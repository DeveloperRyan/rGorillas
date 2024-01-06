package org.ryno.tree.branch

import org.powbot.api.rt4.Camera
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.SimpleLeaf
import org.powbot.api.script.tree.TreeComponent
import org.ryno.Constants
import org.ryno.Script
import org.ryno.State
import org.ryno.tree.leaf.TakeBreak
import org.ryno.utils.InventoryUtils
import org.ryno.utils.Utils

class ShouldBreak(script: Script) : Branch<Script>(script, "Should Break") {
    override val failedComponent: TreeComponent<Script> = ShouldZoomOut(script)
    override val successComponent: TreeComponent<Script> = TakeBreak(script)

    override fun validate(): Boolean {
        return State.shouldBreak && !Utils.playerAtSafespot()
    }
}

class ShouldZoomOut(script: Script) : Branch<Script>(script, "Should Zoom Out") {
    override val failedComponent: TreeComponent<Script> = ShouldKillGorilla(script)
    override val successComponent: TreeComponent<Script> = SimpleLeaf(script, "Zoom Out") {
        script.localLogger.info("Zooming out")
        Camera.moveZoomSlider(0.0)
    }

    override fun validate(): Boolean {
        return Camera.zoom > 15
    }
}