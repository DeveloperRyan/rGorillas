package org.ryno.tree.branch

import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.SimpleLeaf
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager
import org.ryno.*
import org.ryno.Constants
import org.ryno.tree.leaf.*
import org.ryno.tree.leaf.combat.*
import org.ryno.utils.CombatUtils
import org.ryno.utils.InventoryUtils
import org.ryno.utils.Utils

class ShouldKillGorilla(script: Script) : Branch<Script>(script, "Should Fight Gorilla") {
    override val failedComponent: TreeComponent<Script> = ShouldWalkToBank(script)
    override val successComponent: TreeComponent<Script> = ShouldWalkToGorillas(script)

    override fun validate(): Boolean {
        val player = Players.local()

        val shouldKeepFighting = player.inCombat() && player.healthPercent() > 50
        val atBank = Bank.nearest().distanceTo(player) < 5
        val shouldRestock =
            (atBank && Inventory.emptySlotCount() > 0 || !Constants.GORILLAS_AREA.contains(player) && Inventory.emptySlotCount() > 5)
        val shouldHeal = player.healthPercent() < 90 && atBank

        script.localLogger.info(
            "Should Keep Fighting: $shouldKeepFighting - Has Food: ${InventoryUtils.hasFood()} - " +
                    "Has Prayer Potion: ${InventoryUtils.hasPrayerPotion()} - Should Restock: $shouldRestock - Should Heal: $shouldHeal"
        )

        return shouldKeepFighting || (InventoryUtils.hasFood() && (InventoryUtils.hasPrayerPotion() || Prayer.prayerPoints() > 30) && !shouldRestock && !shouldHeal)
    }
}

class ShouldWalkToGorillas(script: Script) : Branch<Script>(script, "Should Walk to Gorillas") {
    override val failedComponent: TreeComponent<Script> = ShouldDodgeBoulder(script)
    override val successComponent: TreeComponent<Script> = WalkToGorillas(script)

    override fun validate(): Boolean {
        return !Constants.GORILLAS_AREA.contains(Players.local())
    }
}

class ShouldDodgeBoulder(script: Script) : Branch<Script>(script, "Should Dodge Boulder") {
    override val failedComponent: TreeComponent<Script> = ShouldChangePrayer(script)
    override val successComponent: TreeComponent<Script> = DodgeBoulder(script)

    override fun validate(): Boolean {
        val projectile = Utils.getFallingBoulder()

        if (!projectile.valid()) {
            return false
        }

        return projectile.destination().tile() == Players.local().trueTile()
    }
}

class ShouldChangePrayer(script: Script) : Branch<Script>(script, "Should Change Prayer") {
    override val failedComponent: TreeComponent<Script> = ShouldEatFood(script)
    override val successComponent: TreeComponent<Script> = ChangePrayer(script)

    override fun validate(): Boolean {
        val gorilla = CombatUtils.getInteractingGorilla()
        val isRoaring = gorilla.valid() && gorilla.overheadMessage() == Constants.GORILLA_ROAR_TEXT && State.roarDelay == 0

        if (isRoaring) {
            script.localLogger.info("Gorilla is roaring; changing prayer")
            State.roarDelay = 4

            when (State.gorillaAttackStyle) {
                Constants.AttackStyles.MELEE -> State.gorillaAttackStyle = Constants.AttackStyles.RANGED
                Constants.AttackStyles.RANGED -> State.gorillaAttackStyle = Constants.AttackStyles.MAGIC
                Constants.AttackStyles.MAGIC -> State.gorillaAttackStyle = Constants.AttackStyles.RANGED
                else -> Constants.AttackStyles.NONE
            }
        }


        val correctPrayer = CombatUtils.getCorrectPrayerForAttackStyle(State.gorillaAttackStyle) ?: return false

        return !Prayer.activePrayers().contains(correctPrayer) && Prayer.prayerPoints() > 0
    }
}

class ShouldEatFood(script: Script) : Branch<Script>(script, "Should Eat Food") {
    override val failedComponent: TreeComponent<Script> = ShouldChangeGear(script)
    override val successComponent: TreeComponent<Script> = EatFood(script)

    override fun validate(): Boolean {
        return Players.local().healthPercent() < script.configuration.eatAtPercent && State.foodDelay <= 0
    }
}

class ShouldChangeGear(script: Script) : Branch<Script>(script, "Should Change Gear") {
    override val failedComponent: TreeComponent<Script> = ShouldPickupLoot(script)
    override val successComponent: TreeComponent<Script> = EquipGear(script)

    override fun validate(): Boolean {
        val gorilla = CombatUtils.getInteractingGorilla()
        val gorillaPrayer = CombatUtils.getActivePrayer(gorilla.prayerHeadIconId())
        val correctAttackStyle = CombatUtils.getCorrectAttackStyleForPrayer(gorillaPrayer)

        script.localLogger.info("Gorilla Prayer: ${gorillaPrayer?.name ?: "None"} - Correct Attack Style: ${correctAttackStyle.name}")

        State.gorillaPrayer = gorillaPrayer
        return !hasCorrectGear(correctAttackStyle)
    }

    private fun hasCorrectGear(attackStyle: Constants.AttackStyles?): Boolean {
        val setup = when (attackStyle) {
            Constants.AttackStyles.RANGED -> script.configuration.rangedSetup
            Constants.AttackStyles.MELEE -> script.configuration.meleeSetup
            else -> State.activeGearSetup
        }
        return Inventory.stream().id(*setup).isEmpty()
    }
}

class ShouldPickupLoot(script: Script) : Branch<Script>(script, "Should Pickup Loot") {
    override val failedComponent: TreeComponent<Script> = ShouldAttackGorilla(script)
    override val successComponent: TreeComponent<Script> = PickupItem(script)

    override fun validate(): Boolean {
        if (State.lootTile == null) {
            return false
        }

        if (Inventory.isFull() && InventoryUtils.hasFood()) {
            val groundItems = GroundItems.stream().id(*Constants.LOOT_WHITELIST)
                .filtered { it.id() != Constants.SHARK_ID }
                .within(State.lootTile!!, 3)

            return groundItems.isNotEmpty()
        } else if (Inventory.isFull() && InventoryUtils.hasPrayerPotion()) {
            val groundItems = GroundItems.stream().id(*Constants.LOOT_WHITELIST)
                .filtered { it.id() != Constants.PRAYER_POTION_3_ID }
                .within(State.lootTile!!, 3)

            return groundItems.isNotEmpty()
        }

        val groundItems = GroundItems.stream()
            .id(*Constants.LOOT_WHITELIST)
            .within(State.lootTile!!, 3)

        return State.shouldLoot && groundItems.isNotEmpty()
    }
}

class ShouldAttackGorilla(script: Script) : Branch<Script>(script, "Should Attack Gorilla") {
    override val failedComponent: TreeComponent<Script> = ShouldAlch(script)
    override val successComponent: TreeComponent<Script> = AttackGorilla(script)

    override fun validate(): Boolean {
        return Constants.GORILLAS_AREA.contains(Players.local()) && !Players.local().interacting().valid()
    }
}

class ShouldAlch(script: Script) : Branch<Script>(script, "Should Alch") {
    override val failedComponent: TreeComponent<Script> = ShouldDrinkPotion(script)
    override val successComponent: TreeComponent<Script> = AlchItem(script)

    override fun validate(): Boolean {
        return Inventory.stream().id(*Constants.ALCH_WHITELIST).isNotEmpty()
                && Magic.Spell.HIGH_ALCHEMY.canCast()
                && script.configuration.shouldAlch
    }
}

class ShouldDrinkPotion(script: Script) : Branch<Script>(script, "Should Drink Potion") {
    override val failedComponent: TreeComponent<Script> = ShouldDropVial(script)
    override val successComponent: TreeComponent<Script> = DrinkPotion(script)

    override fun validate(): Boolean {
        return script.configuration.potions.any { it.shouldDrink() }
    }
}

class ShouldDropVial(script: Script) : Branch<Script>(script, "Should Drop Vial") {
    override val failedComponent: TreeComponent<Script> = SimpleLeaf(script, "In Combat") {
        val player = Players.local()
        script.localLogger.info(
            "In Combat - Gorilla Health: ${
                player.interacting().healthPercent()
            }% - Player Health: ${player.healthPercent()}%"
        )
    }
    override val successComponent: TreeComponent<Script> = DropVial(script)

    override fun validate(): Boolean {
        return Inventory.stream().id(Constants.VIAL_ID).isNotEmpty()
    }
}