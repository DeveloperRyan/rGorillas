package org.ryno.tree.leaf.combat

import org.powbot.api.rt4.Movement
import org.powbot.api.rt4.walking.local.LocalPathFinder
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.utils.CombatUtils
import org.ryno.utils.Utils

class DodgeBoulder(script: Script) : Leaf<Script>(script, "Dodge Boulder") {
    override fun execute() {
        if (!Movement.running() && Movement.energyLevel() > 5) {
            Movement.running(true)
        }

        val projectile = Utils.getFallingBoulder()

        if (projectile.valid()) {
            val ringTiles = Utils.getRingTiles(projectile.destination(), 2)
            val gorilla = CombatUtils.getInteractingGorilla()

            // Get the closest tile to the player that is valid
            val destinationTile = ringTiles.sortedBy { it.distanceTo(CombatUtils.getInteractingGorilla()) }
                .first { it.valid() && LocalPathFinder.findPath(it).isNotEmpty() && it.distanceTo(gorilla) < 6 }

            script.localLogger.info("Dodging Boulder - From: ${projectile.destination()} To: $destinationTile")

            Movement.step(destinationTile)
        }
    }
}