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

class SelectLayers(var layers: Int = 0): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@layers", "I want @layers layers", "I would like @layers layers")
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
class Layers(val count : furhatos.nlu.common.Number? = Number(1) )
