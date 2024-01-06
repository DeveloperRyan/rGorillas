package org.ryno

import com.google.common.eventbus.Subscribe
import org.powbot.api.event.BreakEndedEvent
import org.powbot.api.event.BreakEvent
import org.powbot.api.event.NpcAnimationChangedEvent
import org.powbot.api.event.TickEvent
import org.powbot.api.rt4.Equipment
import org.powbot.api.script.*
import org.powbot.api.script.tree.TreeComponent
import org.powbot.api.script.tree.TreeScript
import org.powbot.mobile.service.ScriptUploader
import org.ryno.models.Configuration
import org.ryno.models.ConfigurationFactory
import org.ryno.tree.branch.ShouldBreak
import org.ryno.utils.CombatUtils
import org.ryno.utils.PaintUtils
import org.ryno.utils.Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ScriptManifest(
    name = "rGorillas",
    description = "Kills demonic gorillas for money",
    version = "2.0.1",
    author = "ryno",
    category = ScriptCategory.MoneyMaking
)
@ScriptConfiguration.List(
    [
        ScriptConfiguration(
            name = "Food Name",
            description = "Food",
            optionType = OptionType.STRING,
            defaultValue = "Manta Ray",
            allowedValues = ["Monkfish", "Karambwan", "Shark", "Manta Ray", "Anglerfish", "Dark Crab"]
        ),
        ScriptConfiguration(
            name = "Eat At Percent",
            description = "HP % to Eat At",
            optionType = OptionType.INTEGER,
            defaultValue = "50"
        ),
        ScriptConfiguration(
            name = "Melee Setup",
            description = "Melee Setup",
            optionType = OptionType.EQUIPMENT,
        ),
        ScriptConfiguration(
            name = "Ranged Setup",
            description = "Ranged Setup",
            optionType = OptionType.EQUIPMENT,
        ),
        ScriptConfiguration(
            name = "Combat Potion Type",
            description = "Combat Potion Type",
            optionType = OptionType.STRING,
            defaultValue = "Super Combat Potion",
            allowedValues = ["None", "Super Set", "Super Combat Potion", "Divine Super Combat Potion"]
        ),
        ScriptConfiguration(
            name = "Combat Potion Amount",
            description = "Combat Potion Amount",
            optionType = OptionType.INTEGER,
            defaultValue = "1"
        ),
        ScriptConfiguration(
            name = "Ranging Potion Type",
            description = "Ranging Potion Type",
            optionType = OptionType.STRING,
            defaultValue = "Ranging Potion",
            allowedValues = ["None", "Ranging Potion", "Divine Ranging Potion"]
        ),
        ScriptConfiguration(
            name = "Ranging Potion Amount",
            description = "Ranging Potion Amount",
            optionType = OptionType.INTEGER,
            defaultValue = "1"
        ),
        ScriptConfiguration(
            name = "Prayer Potion Type",
            description = "Prayer Potion Type",
            optionType = OptionType.STRING,
            defaultValue = "Prayer Potion",
            allowedValues = ["Prayer Potion", "Super Restore Potion"]
        ),
        ScriptConfiguration(
            name = "Prayer Potion Amount",
            description = "Prayer Potion Amount",
            optionType = OptionType.INTEGER,
            defaultValue = "2"
        ),
        ScriptConfiguration(
            name = "Alch Items",
            description = "Should Alch?",
            optionType = OptionType.BOOLEAN,
            defaultValue = "true"
        ),
    ]
)

class Script : TreeScript() {
    val localLogger: Logger = LoggerFactory.getLogger(this::class.java)
    lateinit var configuration: Configuration
    override val rootComponent: TreeComponent<*> = ShouldBreak(this)

    override fun onStart() {
        val meleeSetup = getOption<Map<Int, Equipment.Slot>>("Melee Setup")
        val rangedSetup = getOption<Map<Int, Equipment.Slot>>("Ranged Setup")
        State.activeGearSetup = rangedSetup.keys.toIntArray()

        configuration = ConfigurationFactory.create(
            meleeSetup = meleeSetup.keys.toIntArray(),
            rangedSetup = rangedSetup.keys.toIntArray(),
            foodName = getOption("Food Name"),
            shouldAlch = getOption("Alch Items"),
            eatAtPercent = getOption("Eat At Percent"),
            combatPotionTypeString = getOption("Combat Potion Type"),
            combatPotionCount = getOption("Combat Potion Amount"),
            rangePotionTypeString = getOption("Ranging Potion Type"),
            rangePotionCount = getOption("Ranging Potion Amount"),
            prayerPotionTypeString = getOption("Prayer Potion Type"),
            prayerPotionCount = getOption("Prayer Potion Amount")
        )
        localLogger.info("User Configuration: $configuration")

        addPaint(PaintUtils.createPaint(this))
    }

    @Subscribe
    fun onNpcAnimationChanged(event: NpcAnimationChangedEvent) {
        val npc = event.npc

        val interactingGorilla = CombatUtils.getInteractingGorilla()
        if (npc == State.targetGorilla || npc == interactingGorilla) {
            val animation = event.animation

            when (animation) {
                Constants.DEATH_ANIMATION_ID -> {
                    State.lootTile = npc.trueTile()
                    localLogger.info("Gorilla died; setting loot tile to ${State.lootTile}")

                    State.targetGorilla = null
                    State.shouldLoot = true
                    State.lootCounter = 100
                    State.attackCounter = 0
                    State.killCount += 1
                }

                Constants.MELEE_ATTACK_ANIMATION_ID -> {
                    State.gorillaAttackStyle = Constants.AttackStyles.MELEE
                }

                Constants.RANGED_ATTACK_ANIMATION_ID -> {
                    State.gorillaAttackStyle = Constants.AttackStyles.RANGED
                }

                Constants.MAGIC_ATTACK_ANIMATION_ID -> {
                    State.gorillaAttackStyle = Constants.AttackStyles.MAGIC
                }
            }
        }
    }

    @ValueChanged("Combat Potion Type")
    fun onCombatPotionTypeChange(newValue: String) {
        updateVisibility("Combat Potion Amount", newValue != "None")
    }

    @ValueChanged("Ranging Potion Type")
    fun onRangingPotionTypeChange(newValue: String) {
        updateVisibility("Ranging Potion Amount", newValue != "None")
    }

    @Subscribe
    fun onTick(event: TickEvent) {
        if (State.shouldLoot) {
            State.lootCounter -= 1

            if (State.lootCounter <= 0) {
                localLogger.info("Loot time finished; clearing tile")
                State.shouldLoot = false
                State.lootCounter = 0
            }
        }

        State.potionDelay = maxOf(State.potionDelay - 1, 0)
        State.foodDelay = maxOf(State.foodDelay - 1, 0)
        State.roarDelay = maxOf(State.roarDelay - 1, 0)
    }

    @Subscribe
    fun onBreakEvent(event: BreakEvent) {
        localLogger.info("Got BREAK event")
        State.shouldBreak = true
        if (!Utils.atGrandTree()) {
            localLogger.info("Not at grand tree; delaying break by 5 seconds")
            event.delay(5000)
        } else {
            event.accept()
        }
    }

    @Subscribe
    fun onBreakEnd(event: BreakEndedEvent) {
        localLogger.info("Got BREAK END event")
        State.shouldBreak = false
    }
}

fun main() {
    ScriptUploader().uploadAndStart("rGorillas", "", "localhost:5655", true, false)
}