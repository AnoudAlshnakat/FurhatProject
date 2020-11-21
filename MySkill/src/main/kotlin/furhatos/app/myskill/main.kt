package furhatos.app.myskill

import furhatos.app.myskill.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class MyskillSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
