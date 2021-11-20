package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PdfMaker() {
    companion object {
        fun createPDF(
            text: String?,
            paperType: PaperType,
            context: Context,
            view: View?
        ): Boolean {
            //TODO needes values:
            //Header Mealplaner week
            //From -Till
            //Table columns: date, breakfast, lunch, dinner

            val document = PdfDocument()
            val paint = Paint()
            paint.color = Color.BLUE

            val pageInfo: PdfDocument.PageInfo =
                PdfDocument.PageInfo.Builder(
                    paperType.longSide,
                    paperType.shortSide,
                    1
                ).create()
            val pageOne: PdfDocument.Page = document.startPage(pageInfo)
            val canvas = pageOne.canvas

            canvas.scale(245F / paperType.longSide, 245F / paperType.shortSide)
            Log.d(
                "Numbers",
                "longSide: ${144F / paperType.longSide} short: ${144F / paperType.shortSide}"
            )

            view?.draw(canvas)



            document.finishPage(pageOne)

            return if (DataUtil.savePdfToInternalStorage(context, document)) {
                Log.d("save Private", "Success")
                document.close()
                true
            } else {
                Log.d("save Private", "Error")
                document.close()
                false
            }
        }

    }
}
