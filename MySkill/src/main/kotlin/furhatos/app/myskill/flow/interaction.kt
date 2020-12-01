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
    onReentry{
        furhat.ask("What kind of flavour do you want, chocolate or vanilla?")
    }
    onResponse<SelectFlavour> {
        furhat.say("Great, I love ${it.intent.flavour}!")
        users.current.CakeChoices.cake.flavour = "${it.intent.flavour}"
        goto(ChooseFrosting)
    }
    onResponse {
        furhat.say("I did not catch that.")
       reentry()// Manually propagate to default Dialog state
    }


}

val ChooseFrosting= state(Interaction) {
    onEntry {
        furhat.ask("Would you like to make some frosting with it?")
    }
     onResponse<Yes>{
         users.current.CakeChoices.cake.frosting = true
         furhat.say("Ok great.") //R is recipe and F means Frosting
         goto(ConfirmCake)
    }

    onResponse<No>{
        users.current.CakeChoices.cake.frosting = false
        furhat.say("Alright plain ${users.current.CakeChoices.cake.flavour} cake it is!")
        goto(ConfirmCake)
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
        furhat.say ("You have selected a ${users.current.CakeChoices.cake.flavour}  cake ")
        furhat.say ( "with ${users.current.CakeChoices.cake.layers} layer" )
        furhat.say ((if(users.current.CakeChoices.cake.frosting) "and frosting" else "") )
        //goto(Idle)
        furhat.say("Let me know when you are ready for the ingredients.")
        furhat.listen(timeout = 30000)
    }
    onResponse<Ready> {
        goto(Ingredients)
    }
}

val Ingredients = state(Interaction) {
    onEntry{
        if (users.current.CakeChoices.cake.flavour=="chocolate"){
            furhat.say ( "flour, sugar, cacao, baking powder, salt, vegetable oil")
        }
        else {
            furhat.say("flour, sugar, vanilla extract, baking powder, salt, vegetable oil")
        }
        delay(1000)
        furhat.ask("Do you want to hear the ingredients again or do you want to hear the instructions?")
    }
    onResponse<Ingredients>{
        reentry()
    }
    onResponse<Instructions>{
        goto(GiveInstructions)
    }
}

val GiveInstructions = state(Interaction) {
    var instruction_step = 0

    val instruction = arrayOf("Preheat oven to 350 degrees",
            "In a large bowl, mix  flour, sugar, " +
                    "cocoa and, baking powder.",
                    "Add the vegetable oil, and "+
                    "Mix until smooth ",
                    "Pour the batter into the prepared pan.",
                    "Bake in the oven for 40 minutes")

    onEntry{
        if (users.current.CakeChoices.cake.flavour=="chocolate"){
            val instruction = arrayOf("Preheat oven to 350 degrees",
                    "In a large bowl, mix  flour, sugar, " +
                            "cocoa and, baking powder.",
                    "Add the vegetable oil, and "+
                            "Mix until smooth ",
                    "Pour the batter into the prepared pan.",
                    "Bake in the oven for 40 minutes")}
        else{
            val instruction = arrayOf("Preheat oven to 350 degrees",
                    "In a large bowl, mix  flour, sugar, " +
                            "vanilla extract and, baking powder.",
                    "Add the vegetable oil, and "+
                            "Mix until smooth ",
                    "Pour the batter into the prepared pan.",
                    "Bake in the oven for 40 minutes")}





        furhat.say(instruction.get(0))
        instruction_step=instruction_step+1
        delay(1000)
        furhat.ask("Let me know when you are ready for the net step", timeout = 100000)
    }
    onReentry {
        if (instruction.size - 1 == instruction_step) {
            furhat.say("Ok. Here is the last step")
            delay(1000)
        }
        furhat.say(instruction.get(instruction_step))
        delay(1000)
        instruction_step = instruction_step + 1
        if (instruction.size - 1 < instruction_step) {
            furhat.say("That was the last instruction. Enjoy the cake!")
            goto(Idle)
        } else {
            furhat.ask("Let me know when you are ready for the net step", timeout = 100000)
        }
    }
    onResponse<Ready> { reentry() }
    onResponse<Repeat> {
        instruction_step = instruction_step-1
        reentry() }
}
