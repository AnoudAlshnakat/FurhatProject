package furhatos.app.myskill.nlu

import furhatos.nlu.*
import furhatos.nlu.common.Number
import furhatos.util.Language

class CakeSelection(
        var layers: Int = 1,
        var flavour: String,
        var frosting: Boolean ) : ComplexEnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("@layers", "@flavour", "@frosting")
    }

    override fun toText(): String {
        return generate("")
        //return generate("$layers layers of" + "${flavour?.value}" + "cake " + (if((${frosting?} = true)) "with frosting"; else "without frosting";) )
    }
}

class Flavour : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Vanilla", "Chocolate")
    }
}

class SelectFlavour(var flavour : Flavour? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@flavour", "I want @flavour cake", "I would like @flavour cake", "I want to make @flavour cake")
    }
}

class SelectLayers(
        val count : Number? = Number(1)) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("@count")
    }

    override fun toText(): String {
        return generate("$count")
    }
}




class Ready : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Ready", "Ok, I'm ready", "Ok", "Next")
    }
}
class Ingredients : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Ingredients")
    }
}
class Instructions : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Instructions")
    }
}
class Repeat : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Again", "Repeat")
    }
}

class Done : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Done", "Finished", "completed", "finished", "achieved", "accomplished", "carried out")
    }
}

class Heard : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("heard")
    }
}

class FlavourOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "What flavours do you have?",
                "What are the alternatives?",
                "What do you have?")
    }
}

class FrostingQuestion: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What is frosting?",
                "Could you explain what frosting is?",
                "Frosting?")
    }
}

class Layers(val count : furhatos.nlu.common.Number? = Number(1) )

/*class Layers(val count : furhatos.nlu.common.Number? = Number(1) )


class Continue : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Continue",
                "Go on",
                "Let's continue",
                "Yes",
                "Ready",
                "Start",
                " Next",
                " Next example",
                " Next please")
    }
}


class SelectAllergy(var layers: Int = 0): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I am allergic to @allergy ", "I have a @allergy allergy", "@allergy")
    }
}*/

