package org.ryno.tree.leaf.combat

import org.powbot.api.Condition
import org.powbot.api.rt4.Prayer
import org.powbot.api.script.tree.Leaf
import org.ryno.Script
import org.ryno.State
import org.ryno.utils.CombatUtils

class ChangePrayer(script: Script) : Leaf<Script>(script, "Change Prayer") {
    override fun execute() {
        val correctPrayer = CombatUtils.getCorrectPrayerForAttackStyle(State.gorillaAttackStyle) ?: return

        if (Prayer.prayer(correctPrayer, true)) {
            Condition.wait({ Prayer.prayerActive(correctPrayer) }, 300, 3)

            if (!Prayer.activePrayers().contains(correctPrayer)) {
                script.localLogger.info("Failed to pray ${correctPrayer.name}")
            } else {
                script.localLogger.info("Praying ${correctPrayer.name}")
            }
        }
    }
}