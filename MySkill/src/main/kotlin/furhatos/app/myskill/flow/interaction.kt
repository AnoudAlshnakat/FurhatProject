package furhatos.app.myskill.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.myskill.nlu.*
import furhatos.gestures.Gestures


val Cheerup = state(Interaction) {
    onEntry {
        furhat.say("")
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
        furhat.say( random ("Hi there. I'm Linda, the Virtual Baking Assistant.",
                "Hey hey, I'm Linda",
                "Hello, I am linda, your Baking Assistant"))
        furhat.gesture(Gestures.Wink)

                furhat.ask( random ("Would you like to bake a cake today?",
                        "Are you ready to start baking?",
                        "How about baking a cake today",
                        "May I assist you with an excellent recipe for a cake"),
                        timeout = 10000)
    }
    onResponse<Yes> {
        furhat.gesture(Gestures.Smile)
              furhat.say(random ("Excellent, baking is an adventure for the whole family!",
                    "I love your enthusiasm!",
                    "Wow, that is the spirit",
                    "I knew you would not resist a good cake in this weather",
                    "Nice, let's get creative") )
        goto(ChooseFlavour)
    }

    onResponse<No> {
        furhat.gesture(Gestures.Surprise)
        furhat.say(random ("Okay, that's a shame. Have a splendid day!",
               "I think you made a terrible mistake, we could have had fun",
                "It is okay, I think you will come back later",
                "Too bad, it would have made your kitchen smells better"))
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
        furhat.say( random ("Great, I love ${it.intent.flavour}!",
                "Wonderful choice, it is hard to resist a good cake with ${it.intent.flavour} flavour ",
                "I believe that we will make it taste even better than you imagine",
                "Excellent choice, ${it.intent.flavour} is the flavour of this rainy season"))

        users.current.CakeChoices.cake.flavour = "${it.intent.flavour}"
        goto(ChooseLayers)
    }
    onResponse {
        furhat.say("I did not catch that.")
       reentry()// Manually propagate to default Dialog state
    }


}
val ChooseLayers= state(Interaction) {
    onEntry {
        furhat.ask(random ("How many layers do you want for your cake?",
                                 "How many layers is your cake going to be?",
                                 "How many cake layers do you have in mind?"))
    }
    onResponse<SelectLayers>{
        users.current.CakeChoices.cake.layers = it.intent.toString().toInt()
        print(users.current.CakeChoices.cake.layers)
        furhat.say( random("Ok you want ${it.intent} layers",
                                "Oh, that is great, ${it.intent} layers is a nice choice ",
                                "Okay, it seems like ${it.intent} layers for today's cake")) //R is recipe and F means Frosting
        goto(ChooseFrosting)
    }
}

val ChooseFrosting= state(Interaction) {
    onEntry {
        furhat.ask(random ("Would you like to make some frosting with it?",
                                "How about making extra frosting with it? do you want frosting?",
                                "What about frosting, I think it goes well with my recipe. Would you like to make it?",
                                "Would you like to prepare some frosting on the side?"))
    }
     onResponse<Yes>{
         users.current.CakeChoices.cake.frosting = true
         furhat.say(random ("Ok great.",
                                "Nice, you have a fine taste",
                                "Frosting makes everything taste better!",
                                "Wonderful, I love frosting.") )

         goto(ConfirmCake)
    }

    onResponse<No>{
        users.current.CakeChoices.cake.frosting = false
        furhat.say( random ("Alright plain ${users.current.CakeChoices.cake.flavour} cake it is!",
                                    "As you wish, I still can make this recipe work, even without frosting",
                                    "Too bad, it would have tasted good with frosting"))
        goto(ConfirmCake)
    }

}

val ConfirmCake = state(Interaction) {
    var ready = true; //assume always ready to hear the ingredients
    onEntry{
        if (ready) {
            furhat.say("You have selected a ${users.current.CakeChoices.cake.layers} layer  ${users.current.CakeChoices.cake.flavour}  cake "+
                    (if (users.current.CakeChoices.cake.frosting) " with frosting. " else ""))
            furhat.say("Let me know when you are ready for the cake ingredients.")
            furhat.listen(timeout = 5000)
        }
        else{
            furhat.say("take a few minutes, I am waiting here.")
            furhat.listen(timeout = 60000)
        }
    }
    onReentry {furhat.say{"Let me know when you are ready."}
        furhat.listen(timeout=30000)
    }
    onNoResponse {
        call(Cheerup)
        reentry()
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
                furhat.say ( "${170    * users.current.CakeChoices.cake.layers} grams flour," +
                              " ${200  * users.current.CakeChoices.cake.layers} grams sugar,"+
                              " ${4  * users.current.CakeChoices.cake.layers} table spoons of cacao,"+
                              "${2  * users.current.CakeChoices.cake.layers} table spoons of baking powder,"+
                              "${2  * users.current.CakeChoices.cake.layers} pinches of salt and"+
                              "${280  * users.current.CakeChoices.cake.layers} grams of vegetable oil.")
            }
            else {
                furhat.say("${170    * users.current.CakeChoices.cake.layers} grams flour, " +
                        " ${200  * users.current.CakeChoices.cake.layers} grams sugar, "+
                        " ${1  * users.current.CakeChoices.cake.layers} table spoons of vanilla extract, "+
                        "${2  * users.current.CakeChoices.cake.layers} table spoons of baking powder, "+
                        "${2  * users.current.CakeChoices.cake.layers} pinches of salt and "+
                        "${280  * users.current.CakeChoices.cake.layers} grams of vegetable oil.")
            }
            delay(2000)
            furhat.ask(random ("Do you want to hear the ingredients again or do you want to start with the instructions?",
                                    "I would like to start with the instructions now, do you want me to repeat the ingredients or start with the instructions"))
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
                    "vanilla extract and, baking powder.",
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

        furhat.say(instruction.get(0))
        instruction_step=instruction_step+1
        delay(1000)

        furhat.ask(random("Let me know when you are ready for the next step.",
                "Let me know when you are ready for the following step.",
                "Whenever you are done, I can move to the next step, just let me know",
                "Once you are done with this step, please inform me",
                "Great, whenever you are ready for the next phase of the recipe ",
                "This procedure supposed to be straight forward, When you are ready for the next one, tell me",
                "If you are done and ready for the next step, just say that you are ready",
                "If you are done and ready for the following step, just say that you are ready",
                "When you are ready to the following step please tell me"
        ),

                timeout = 100000)
    }
    onReentry {
        if (instruction.size - 1 == instruction_step) {
            furhat.say("Ok. Here is the last step.")
            delay(2000)
        }
        furhat.say(instruction.get(instruction_step))
        delay(2000)
        instruction_step = instruction_step + 1
        if (instruction.size - 1 < instruction_step) {
            furhat.say(random ("That was the last instruction of the cake, I am very excited for the results!",
                                    "And this was the very last step, I can't wait for the results",
                                    "That was the last instruction of my recipe, Let's see how the results will smell and look like",
                                    "That was my last instruction of the cake, I am pretty curios to see your face once it is done"))




            goto(ReadyForFrosting) //change enjoy the cake, and go to idle, to an other step to check it ith frosting
        } else {

                    furhat.ask(random("Let me know when you are ready for the next step.",
                                "Are you ready for the following step?",
                                "Tell me when you are done with that."), timeout = 100000)
        }
    }
    onResponse<Ready> { reentry() }
    onResponse<Done> { reentry() }
    onResponse<Yes> { reentry() }
    onResponse<Repeat> {
        instruction_step = instruction_step-1
        reentry() }
    onResponse<No> {
        furhat.say("No problem, I can wait a bit. Just tell me when you are done")
        furhat.listen(timeout = 300000)
        reentry()
    }
}


val ReadyForFrosting = state(Interaction) {
    onEntry{
        if (users.current.CakeChoices.cake.frosting){
            call(Cheerup)
        furhat.ask (random("Are you ready to fetch the frosting ingredients?",
                                "How about we start with the frosting ingredients?",
                                "I would like to start now with the frosting. what about you, do you want to start?",
                                "I think it is time to start preparing frosting, do you want to hear the ingredients?",
                                "Shall we start with the frosting ingredients?"))
        furhat.listen(timeout = 30000)}
        else
        {goto(Done)}
    }
    onResponse<Ready> {
        goto(FrostingIngredients)
    }
    onResponse<Yes> {
        goto(FrostingIngredients)
    }

    onResponse<No> {
        furhat.say("No problem, I can wait a bit.")
        furhat.listen(timeout = 30000)
        reentry()
    }
}


val FrostingIngredients = state(Interaction){
    onEntry{
        furhat.say("Bring" +
                " ${75  * users.current.CakeChoices.cake.layers} grams butter, "+
                " ${360  * users.current.CakeChoices.cake.layers} grams powdered sugar, "+
                "${1  * users.current.CakeChoices.cake.layers} table spoon of vanilla extract and "+
                "${2  * users.current.CakeChoices.cake.layers} table spoons of milk. ")
        //recipe source : https://www.bettycrocker.com/recipes/vanilla-buttercream-frosting/39107a19-be94-4571-9031-f1fc5bd1d606



        delay(2000)
        furhat.ask ( random ("Would you like to hear the ingredients again?",
                                    "I can repeat the ingredients if you like",
                                    "If you did not hear the ingredients I can repeat them again"))

    }

    onResponse<Yes> {
        reentry()
    }
    onResponse<Repeat> {
        reentry()
    }
    onResponse<Heard> {
        furhat.say ( "I am glad that I did not have to repeat that, phewww")
        goto(FrostingInstruction)
    }
    onResponse<No> {
        furhat.say ( random("Alright then! let's start making the frosting.",
                "I am glad that I did not have to repeat the ingredients, you seem smart."))
        goto(FrostingInstruction)
    }

}

val FrostingInstruction = state(Interaction) {
    var counter = 0
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
                furhat.ask(random("Let me know when you are done, so we can start the next step.",
                        "Let me know when you are ready for the following step.",
                        "Whenever you are done, I can move to the next step, just let me know",
                        "Once you are done with this step, please inform me",
                        "Great, whenever you are ready for the next phase of the recipe ",
                        "This procedure supposed to be straight forward, When you are ready for the next one, tell me",
                        "If you are done and ready for the next step, just say that you are ready",
                        "If you are done and ready for the following step, just say that you are ready",
                        "When you are ready to the following step please tell me"
                ),

                        timeout = 100000)
            else {
                furhat.say(random("Now the frosting is done, put it on the side, and wait for your cake to be ready.",
                "Great, and that is how you make a frosting. Let's wait for the cake be be ready now.",
                "Excellent, I knew you can do it! The frosting is done."))
                goto(Layering)
            }
        }
    }
    onResponse<Ready> { reentry() }
    onResponse<Repeat> { furhat.say(Finstruction.get(counter-1))
        delay(3000)
        furhat.ask("If you are done and ready for the next step, just say that you are ready", timeout = 100000)
        reentry()
    }
}

val Layering = state(Interaction) {
    onEntry {
        furhat.say("I bet that you can start smelling the cake, I will get a sense of smell soon in the future")
        furhat.say("Take the cake out of the oven and cut it carefully to get ${users.current.CakeChoices.cake.layers} layers.")
        furhat.say("Spread the frosting evenly between each layer and put it back together. Finally cover the cake in frosting.")
        goto(Done)
    }

}


val Done = state(Interaction) {
    onEntry {
        call(Cheerup)
        furhat.say("Enjoy the cake!")
        goto(Idle)
    }

}


