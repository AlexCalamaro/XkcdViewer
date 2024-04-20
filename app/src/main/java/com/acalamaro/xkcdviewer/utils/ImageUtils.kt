package com.acalamaro.xkcdviewer.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Transformation


class ImageUtils {
    companion object {

        /**
         * Implementation of Transformation interface in Picasso
         * Allows arbitrary modification of image after loading
         * but before displaying to a view
         */
        class InversionTransformation : Transformation {
            override fun transform(source: Bitmap?): Bitmap {
                val result = invertBitmap(source!!)
                source.recycle()
                return result
            }
            override fun key(): String {
                return "invert"
            }
        }

        // Determine if Night/Dark mode is enabled on device
        fun isNightModeEnabled(context: Context) : Boolean {
            return when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        }

        // Invert the contents of an ImageView if night mode is enabled
        fun invertImageViewIfNightMode(imageView: AppCompatImageView) {
            if(isNightModeEnabled(imageView.context)) {
                val drawable = imageView.drawable
                val src = drawable.toBitmap()

                imageView.setImageBitmap(invertBitmap(src))
            }
        }

        /**
         * @param src The bitmap to invert
         * @return inverted bitmap
         * This method converts a source bitmap to greyscale and then inverts it,
         * providing eye-friendly comics for dark mode
         */
        private fun invertBitmap(src : Bitmap) : Bitmap{
            val height = src.height
            val width = src.width

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()

            val matrixGrayscale = ColorMatrix()
            matrixGrayscale.setSaturation(0f)

            val matrixInvert = ColorMatrix()
            matrixInvert.set(floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            )
            matrixInvert.preConcat(matrixGrayscale)

            val filter = ColorMatrixColorFilter(matrixInvert)
            paint.colorFilter = filter

            canvas.drawBitmap(src, 0f, 0f, paint)
            return bitmap
        }
    }
}