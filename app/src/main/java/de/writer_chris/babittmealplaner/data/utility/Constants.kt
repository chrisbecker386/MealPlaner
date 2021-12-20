package de.writer_chris.babittmealplaner.data.utility

import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.Dish

enum class MealTypes(val title: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner")
}

enum class PaperType(val longSide: Int, val shortSide: Int) {
    //resource
    //http://prepessure.com/libary/paper-size
    A4(842, 595),
    US_LETTER(792, 612)
}

enum class FileName(val fileString: String) {
    TEMPORAL_NAME("temp"),
    INTERNAL_PDF_NAME("MealPlan.pdf"),
    EXTERNAL_PDF_NAME("MealPlan"),
    PDF("pdf"),
    JPG("jpg")
}


enum class PermissionCode(val requestCode: Int) {
    WRITE_EXTERNAL(0),
    READ_EXTERNAL(1),
    LOCATION_BACKGROUND(2)
}

enum class StorageAccessFrameworkCode(val requestCode: Int) {
    OPEN_REQUEST_CODE(41)
}

enum class ErrorMessage(val resId: Int) {
    ERROR_PDF_SAVE(R.string.dialog_save_error_occured),
    ERROR_PDF_SHARE(R.string.dialog_share_error_occured)
}

enum class SharePrefState(
    val key: String,
    val value: Boolean
) {
    APP_START_FIRST_TIME(key = "isAppAlreadyRun", value = false),
    APP_START_REGULAR(key = "isAppAlreadyRun", value = true)
}


const val MINIMAL_DAY = 1
const val MAXIMAL_DAY = 8
const val PIXA_BAY_BASE_URL = "https://pixabay.com/"
const val CHANNEL_ID = "MealPlanChannelID"
const val NOTIFICATION_ID = 0

val DISH_LIST_STARTUP = listOf(
    Dish(
        1, "Spaghetti Bolognese", 25, "1 Zwiebel\n" +
                "1 Zehe Knoblauch\n" +
                "1 Möhre\n" +
                "500 g Hackfleisch (Rinderhack oder Tartar)\n" +
                "Salz und Pfeffer\n" +
                "200 ml Gemüsebrühe (Instant)\n" +
                "70 g Tomatenmark\n" +
                "1 TL Oregano\n" +
                "400 g Tomate(n), gestückelt, mit Kräutern, z.B. aus dem Tetrapack\n" +
                "2 EL Tomatenketchup\n" +
                "500 g Nudeln (Spaghetti)\n\n" +
                "Zwiebel, Knoblauch und Möhre schälen und in feine Würfel schneiden. Hackfleisch in die Pfanne geben, langsam erhitzen und im eigenen Fett unter Rühren anbraten. Salzen und pfeffern. Zwiebeln, Knoblauch und Möhren dazugeben und kurz mitbraten. Mit der Brühe ablöschen, Tomatenmark, Oregano, die gestückelten Tomaten und Tomatenketchup unterrühren. Etwa 40 Minuten einkochen lassen.\n" +
                "Spaghetti in Salzwasser bissfest kochen, abgießen, abschrecken und zusammen mit der Sauce servieren.\n" +
                "Source: https://www.chefkoch.de/rezepte/393031127655461/Spaghetti-Bolognese.html"
    ),
    Dish(
        2,
        "Pfannenkuchen",
        25,
        "1 Cup Tasse\n1 Ei\n1 Tasse Milch\n\nAlle Zutaten ordentlich verrühren. Der Pfanne entsprechend dosiert in einer Pfanne  mit etwas Butter anbraten und wenden"
    )
)

