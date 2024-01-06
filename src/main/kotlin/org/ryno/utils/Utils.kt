package org.ryno.utils

import org.powbot.api.Condition
import org.powbot.api.Notifications
import org.powbot.api.Tile
import org.powbot.api.rt4.*
import org.powbot.mobile.script.ScriptManager
import org.ryno.Constants
import org.ryno.State
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Utils {
    val utilsLogger: Logger = LoggerFactory.getLogger(Utils::class.java)

    fun atGrandTree(): Boolean {
        val player = Players.local()

        return Constants.GRAND_TREE_AREA_LOWER.contains(player) ||
                Constants.GRAND_TREE_AREA_UPPER.contains(player)
    }

    fun getFallingBoulder(): Projectile {
        return Projectiles.stream().within(2).id(Constants.BOULDER_PROJECTILE_ID).first()
    }

    fun handleGrandTreeTeleport(): Boolean {
        if (atGrandTree()) {
            return true
        }

        val teleportSeed = InventoryUtils.getInventoryItem(Constants.ROYAL_SEED_POD_NAME)
        if (teleportSeed.valid() && teleportSeed.interact("Commune")) {
            utilsLogger.info("Teleporting to Grand Tree")
            Condition.wait ({ atGrandTree() && Players.local().animation() == -1 }, 300, 8 )
        }

        return atGrandTree()
    }

    fun playerAtSafespot(): Boolean {
        return Players.local().tile() == Constants.GORILLAS_SAFESPOT_TILE
    }

    fun walkToSafespot() {
        val safespot = Constants.GORILLAS_SAFESPOT_TILE

        utilsLogger.info("Walking to safespot at $safespot")
        Movement.builder(safespot).setRunMin(1).setRunMax(3).setAutoRun(true).move()
    }

    fun handleUnchargedItems() {
        if (!State.hasChargedItems) {
            return
        }

        val unchargedItemCount = Inventory.stream().filtered {
            it.id() in Constants.UNCHARGED_ITEMS
        }.count() + Equipment.stream().filtered {
            it.id() in Constants.UNCHARGED_ITEMS
        }.count()

        if (unchargedItemCount > 0) {
            utilsLogger.info("Uncharged items found; teleporting and stopping")
            if (!handleGrandTreeTeleport()) {
                utilsLogger.error("Failed to handle Grand Tree teleport for uncharged items")
                walkToSafespot()
            }

            Notifications.showNotification("Out of charged; stopping script.")
            ScriptManager.stop()
            return
        }
    }

    fun Tile.rangeTo(other: Tile): List<Tile> {
        val xRange = this.x()..other.x()
        val yRange = this.y()..other.y()

        return xRange.flatMap { x ->
            yRange.map { y ->
                Tile(x, y)
            }
        }
    }

    fun getRingTiles(center: Tile, radius: Int): List<Tile> {
        val allTiles = center.derive(-radius, -radius).rangeTo(center.derive(radius, radius))
        val innerTiles = center.derive(-(radius - 1), -(radius - 1)).rangeTo(center.derive(radius - 1, radius - 1))
        return allTiles - innerTiles.toSet()
    }
}