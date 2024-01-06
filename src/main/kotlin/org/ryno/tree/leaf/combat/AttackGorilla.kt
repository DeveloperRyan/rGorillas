package org.ryno.tree.leaf.combat

import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.ryno.Constants
import org.ryno.Script
import org.ryno.State
import org.ryno.utils.Utils.handleUnchargedItems

class AttackGorilla(script: Script) : Leaf<Script>(script, "Attack Gorilla") {
    override fun execute() {
        handleUnchargedItems()

        if (!Movement.running() && Movement.energyLevel() > 5) {
            Movement.running(true)
        }

        var gorilla = Npcs.stream()
            .name(Constants.DEMONIC_GORILLA_NAME)
            .within(Constants.GORILLAS_AREA)
            .interactingWithMe()
            .nearest().first()

        if (!gorilla.valid()) {
            gorilla = Npcs.stream().name(Constants.DEMONIC_GORILLA_NAME)
                .within(Constants.GORILLAS_AREA)
                .filtered { !it.inCombat() }.nearest().first()
        }

        if (!gorilla.valid()) {
            script.localLogger.info("No valid gorilla found")
        }

        if (!gorilla.inViewport() && gorilla.distance() < 10) {
            Game.tab(Game.Tab.NONE)
            Camera.turnTo(gorilla)
        }

        if (gorilla.interact("Attack")) {
            script.localLogger.info("Attacking Gorilla at tile ${gorilla.trueTile()}")
            Condition.wait({ Players.local().interacting() == gorilla }, 300, 10)

            if (Players.local().interacting() == gorilla) {
                script.localLogger.info("Successfully attacked Gorilla")
                State.targetGorilla = gorilla
            }
        } else {
            script.localLogger.info("Failed to attack Gorilla At Tile ${gorilla.trueTile()} - Valid: ${gorilla.valid()} - In viewport: ${gorilla.inViewport()}")
        }
    }
}