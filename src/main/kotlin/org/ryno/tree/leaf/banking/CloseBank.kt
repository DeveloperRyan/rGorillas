package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.script.tree.Leaf
import org.ryno.Script

class CloseBank(script: Script) : Leaf<Script>(script, "Close Bank") {
    override fun execute() {
        script.localLogger.info("Closing bank")
        if (Bank.opened() && Bank.close()) {
            Condition.wait({ !Bank.opened() }, 300, 4)
        }
    }
}