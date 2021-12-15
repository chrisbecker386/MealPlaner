package de.writer_chris.babittmealplaner.data.utility


import de.writer_chris.babittmealplaner.R

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

const val MINIMAL_DAY = 1
const val MAXIMAL_DAY = 8

const val PIXA_BAY_BASE_URL = "https://pixabay.com/"

