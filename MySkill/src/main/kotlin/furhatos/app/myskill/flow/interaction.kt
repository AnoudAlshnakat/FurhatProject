package furhatos.app.myskill.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.myskill.nlu.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.say("Hi there. I'm Linda, the Virtual Baking Assistant.")
        random(
                { furhat.ask("Would you like to bake a cake today?") },
                { furhat.ask("Are you ready to start baking?") }
        )
    }
    onResponse<Yes> {
        random(
                { furhat.say("Excellent, baking is an adventure for the whole family!") },
                { furhat.say("I love your enthusiasm!") }
        )
        goto(ChooseFlavour)
    }

    onResponse<No> {
        furhat.say("Okay, that's a shame. Have a splendid day!")
        goto(Idle)
    }
}
val ChooseFlavour = state(Interaction) {
    onEntry{
        furhat.ask("What kind of flavour do you want your cake to have?")
    }
    onResponse<SelectFlavour> {
        furhat.say("Great, I love ${it.intent.flavour}!")
        users.current.CakeChoices.cake.flavour = "${it.intent.flavour}"
        goto(ChooseFrosting)
    }
}

val ChooseFrosting= state(Interaction) {
    onEntry {
        furhat.ask("Would you like to make some frosting with it?")
    }
     onResponse<Yes>{
         users.current.CakeChoices.cake.frosting = true
         furhat.say("Ok great.") //R is recipe and F means Frosting
         goto(ChooseLayers)
    }

    onResponse<No>{
        users.current.CakeChoices.cake.frosting = false
        furhat.say("Alright plain ${users.current.CakeChoices.cake.flavour} cake it is!")
        goto(ChooseLayers)
    }

}
val ChooseLayers= state(Interaction) {
    onEntry {
        furhat.ask("How many layers do you want for your cake?")
    }
    onResponse<SelectLayers>{
        users.current.CakeChoices.cake.layers = it.intent.layers.toString().toInt()
        furhat.say("Ok you want ${it.intent.layers} layers") //R is recipe and F means Frosting
        goto(ConfirmCake)
    }


}

val ConfirmCake = state(Interaction) {
    onEntry{
        furhat.say { "You have selected a ${users.current.CakeChoices.cake.flavour}  cake "}
        //furhat.say { "with ${users.current.CakeChoices.cake.layers} layers" }
        //furhat.say { (if(users.current.CakeChoices.cake.frosting) "and frosting" else "") }
        //goto(Idle)
    }
}

