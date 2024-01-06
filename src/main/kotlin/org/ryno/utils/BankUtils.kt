package org.ryno.utils

import org.powbot.api.Notifications
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Item
import org.powbot.mobile.script.ScriptManager

object BankUtils {
    private fun getBankItem(name: String): Item {
        val item = Bank.stream().name(name).first()
        Utils.utilsLogger.info("Got bank item: ${item.name()}")

        return item
    }

    fun getCorrectPotionFromBank(baseName: String): Item {
        val fourDose = getBankItem("$baseName(4)")
        val threeDose = getBankItem("$baseName(3)")

        if (fourDose.valid()) {
            return fourDose
        } else if (threeDose.valid()) {
            return threeDose
        } else {
            Utils.utilsLogger.info("Could not find $baseName in bank")
            Notifications.showNotification("Out of $baseName; stopping")
            ScriptManager.stop()
        }

        return Item.Nil
    }
}