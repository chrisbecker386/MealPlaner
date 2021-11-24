package de.writer_chris.babittmealplaner.data.utility

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

const val TEMPORAL_FILE_NAME = "temp"
const val INTERNAL_PDF_FILE_NAME = "MealPlan.pdf"
const val EXTERNAL_PDF_FILE_NAME = "MealPlan"
const val SUB_FOLDER_NAME = "my_pdfs"


enum class PermissionCode(val requestCode: Int) {
    WRITE_EXTERNAL(0),
    READ_EXTERNAL(1),
    LOCATION_BACKGROUND(2)
}

enum class StorageAccessFrameworkCode(val requestCode: Int) {
    OPEN_REQUEST_CODE(41)
}