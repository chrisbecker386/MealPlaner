package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.view.View
import de.writer_chris.babittmealplaner.data.utility.FileName.*

class PdfMaker {
    companion object {
        fun createPDF(
            dateText: String,
            paperType: PaperType,
            context: Context,
            view: View?,
            isDownload: Boolean
        ): Boolean {
            val document = PdfDocument()

            val pageInfo: PdfDocument.PageInfo =
                PdfDocument.PageInfo.Builder(
                    paperType.longSide,
                    paperType.shortSide,
                    1
                ).create()
            val pageOne: PdfDocument.Page = document.startPage(pageInfo)
            val canvas = pageOne.canvas

            canvas.scale(245F / paperType.longSide, 245F / paperType.shortSide)
            view?.draw(canvas)

            document.finishPage(pageOne)

            val filename = "${EXTERNAL_PDF_NAME.fileString}$dateText${PDF.fileString}"
            if (isDownload) {
                return if (DataUtil.writePdfToDownloads(
                        context,
                        document,
                        filename
                    )
                ) {
                    document.close()
                    true
                } else {
                    document.close()
                    false
                }
            } else {
                return if (DataUtil.savePdfToInternalStorage(context, document)) {
                    document.close()
                    true
                } else {
                    document.close()
                    false
                }
            }
        }


    }
}
