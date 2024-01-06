package org.ryno.utils

import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.api.script.paint.TrackInventoryOption
import org.powbot.api.script.paint.TrackSkillOption
import org.powbot.mobile.script.ScriptManager
import org.ryno.Constants.ZENYTE_ID
import org.ryno.Script
import org.ryno.State

object PaintUtils {
    fun createPaint(script: Script): Paint {
        return PaintBuilder.newBuilder()
            .x(40)
            .y(45)
            .trackSkill(Skill.Ranged, "Ranged XP", TrackSkillOption.Exp)
            .trackSkill(Skill.Hitpoints, "Hitpoints XP", TrackSkillOption.Exp)
            .trackSkill(Skill.Attack, "Attack XP", TrackSkillOption.Exp)
            .trackSkill(Skill.Strength, "Strength XP", TrackSkillOption.Exp)
            .trackSkill(Skill.Defence, "Defence XP", TrackSkillOption.Exp)
            .trackInventoryItem(ZENYTE_ID, "Zenyte Shards", TrackInventoryOption.QuantityChangeIncOny)
            .addString("Status: ") { if (State.shouldBreak) "Breaking" else script.lastLeaf.toString() }
            .addString("Kills: ") { State.killCount.toString() }
            .addString("Kills/hr: ") { getKillsPerHour().toString() }
            .build()
    }

    private fun getKillsPerHour(): Int {
        val runtime = ScriptManager.getRuntime().toDouble() / 3600000
        val killsPerHour = State.killCount / runtime

        return killsPerHour.toInt()
    }
}