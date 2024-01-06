package org.ryno.tree.leaf.banking

import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.script.tree.Leaf
import org.ryno.Script

class OpenBank(script: Script) : Leaf<Script>(script, "Open Bank") {
    override fun execute() {
        if (Bank.open()) {
            Condition.wait({ Bank.opened() }, 300, 8)
        }
    }
}