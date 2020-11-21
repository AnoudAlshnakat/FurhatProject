package furhatos.app.myskill.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.myskill.nlu.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there. Would you like to start making a cake today?")
    }

    onResponse<Yes>{
       // furhat.say("I like humans.")
       goto(ChooseRandF) //R is recipe and F means Frosting
    }

    onResponse<No>{
        furhat.say("That's sad.")
    }
}


val ChooseRandF = state(Interaction) {

    onEntry {
        furhat.ask("Would you like to make some frosting with it?")
    }

     onResponse<Yes>{
       // furhat.say("I like humans.")
       furhat.say("Ok shall we start now?") //R is recipe and F means Frosting
    }

    onResponse<No>{
        furhat.say("That's sad.")
    }

}