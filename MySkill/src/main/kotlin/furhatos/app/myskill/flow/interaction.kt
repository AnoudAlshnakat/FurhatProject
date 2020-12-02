package furhatos.app.myskill.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.myskill.nlu.*
import furhatos.gestures.Gestures


val Cheerup = state(Interaction) {
    onEntry {
        furhat.say {
            random {
                + "Don't give up, it is just a cake."
                + "I bet Gordon Ramsy will not like this cake, looks a bit whimsy, work harder"
                +""
                + "Baking is not for quitters"
                + "Good Job, I see the light at the end of the tunnel. Finally!"
                + ""
                + "OMG, This is going so slow, can you be a little bit faster"
                + "Move It, Grandpa"
                + "Move It, Grandma"
                + "You do seriously surprise me"
            }
        }
        furhat.gesture(Gestures.Nod)
        terminate()
    }
}


val Start : State = state(Interaction) {

    onEntry {
        furhat.say("Hi there. I'm Linda, the Virtual Baking Assistant.")
        furhat.gesture(Gestures.Wink)
        random(
                { furhat.ask("Would you like to bake a cake today?") },
                { furhat.ask("Are you ready to start baking?") }
        )

    }
    onResponse<Yes> {
        furhat.gesture(Gestures.Smile)
        random(
                { furhat.say("Excellent, baking is an adventure for the whole family!") },
                { furhat.say("I love your enthusiasm!") }
        )
        goto(ChooseFlavour)
    }

    onResponse<No> {
        furhat.gesture(Gestures.Surprise)
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
    var ready = true; //assume always ready to hear the ingredients
    onEntry{
        if (ready) {
            furhat.say("You have selected a ${users.current.CakeChoices.cake.flavour}  cake ")
            furhat.say("with ${users.current.CakeChoices.cake.layers} layer")
            furhat.say((if (users.current.CakeChoices.cake.frosting) "and frosting" else ""))
            furhat.say("Let me know when you are ready for the cake ingredients.")
            furhat.listen(timeout = 30000)
        }
        else{
            furhat.say("take a few minutes, I am waiting here.")
            furhat.listen(timeout = 60000)
        }
    }
    onResponse<Ready> {
       // goto(Ingredients)
        goto(Ingredients)
    }
    onResponse<Yes> {
        // goto(Ingredients)
        goto(Ingredients)
    }
    onResponse<No> {
        ready = false; //if not ready
        reentry()
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
            furhat.ask("Do you want to hear the ingredients again or do you want to start with the instructions?")
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
                    "Pour the butter into the prepared pan.",
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
        random(
        furhat.ask("Let me know when you are ready for the next step", timeout = 100000),
        furhat.ask("Let me know when you are ready for the following step", timeout = 100000)
                )
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
            furhat.say("That was the last instruction of the cake, I am very excited for the results!")
            goto(ReadyForFrosting) //change enjoy the cake, and go to idle, to an other step to check it ith frosting
        } else {
            random(
                    furhat.ask("Let me know when you are ready for the next step", timeout = 100000),
                    furhat.ask("Are ready for the following step?", timeout = 100000)
            )
        }
    }
    onResponse<Ready> { reentry() }
    onResponse<Repeat> {
        instruction_step = instruction_step-1
        reentry() }
}


val ReadyForFrosting = state(Interaction) {
    onEntry{
        if (users.current.CakeChoices.cake.frosting){
            call(Cheerup)
           // reentry()
        furhat.ask ("Are you ready to fetch the frosting ingredients?")
        furhat.listen(timeout = 30000)}
        else
        {goto(finally)}
    }
    onResponse<Ready> {
        goto(FrostingIngredients)
    }
    onResponse<Yes> {
        goto(FrostingIngredients)
    }

    onResponse<No> {
        reentry()
    }
}


val FrostingIngredients = state(Interaction){
    onEntry{
        furhat.say ( "Bring butter, powdered  sugar, vanilla, milk.")
        delay(2000)
        furhat.ask ( "Would you like to hear the ingredients again?")
    }

    onResponse<Yes> {
        reentry()
    }
    onResponse<Repeat> {
        reentry()
    }
    onResponse<No> {
        furhat.say ( "Alright then! let's start making the frosting.")
        goto(FrostingInstruction)
    }

}

val FrostingInstruction = state(Interaction) {
    var counter = 0;
    val Finstruction = arrayOf("mix powdered sugar and butter with spoon or electric mixer on low speed.",
            "Stir in vanilla and 1 tablespoon of the milk.",
            "Gradually beat in just 1 table spoon of milk .")

    onEntry {
        if (counter < 3) {
            furhat.say(Finstruction.get(counter))
            counter = counter + 1
            delay(500)
            //furhat.listen(timeout = 1000)
            if (counter < 3)
                furhat.ask("When you are ready for the next step, please tell me", timeout = 100000)
            else {
                furhat.say("Now the frosting is done, put it on the side, and wait for your cake to be ready.")
                goto(finally)
            }
        }
        else {

        }
    }
    onResponse<Ready> { reentry() }
    onResponse<Repeat> { furhat.say(Finstruction.get(counter-1))
        delay(3000)
        furhat.ask("When you are ready for the next step, please tell me", timeout = 100000)
        reentry()
    }
}



val finally = state(Interaction) {
    onEntry {
        furhat.say("I bet that you can start smelling the cake, I will get a sense of smell soon in the future")
        goto(Idle)
    }

}


