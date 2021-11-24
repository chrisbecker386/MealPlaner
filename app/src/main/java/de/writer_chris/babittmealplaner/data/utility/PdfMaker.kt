package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.view.View


class PdfMaker() {
    companion object {
        fun createPDF(
            text: String,
            paperType: PaperType,
            context: Context,
            view: View?,
            isDownload: Boolean
        ): Boolean {

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

//            return if (DataUtil.savePdfToInternalStorage(context, document)) {
//                Log.d("save Private", "Success")
//                document.close()
//                document
//            } else {
//                Log.d("save Private", "Error")
//                document.close()
//                document
//            }
            val filename = EXTERNAL_PDF_FILE_NAME + text + ".pdf"

            if (isDownload) {
                return if (DataUtil.writePdfToDownloads(
                        context,
                        document,
                        filename
                    )
                ) {
                    Log.d("save Private", "Success")
                    document.close()
                    true
                } else {
                    Log.d("save Private", "Error")
                    document.close()
                    false
                }
            } else {
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
}
