package furhatos.app.myskill.flow


import furhatos.app.myskill.nlu.CakeSelection
import furhatos.app.myskill.nlu.Flavour
import furhatos.records.User

class CakeData (
        var cake : CakeSelection
)



val User.CakeChoices : CakeData
   get() = data.getOrPut(CakeData::class.qualifiedName, CakeData(CakeSelection(1, "Vanilla", false)))
