package com.squidink.xkcdviewer.extensions

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.graphics.RectF
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.ortiz.touchview.TouchImageView
import kotlin.math.min

fun ImageView.spinForever() {
    ObjectAnimator
        .ofFloat(this, "rotation", 0f, 360f)
        .apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
            start()
        }
}

fun TouchImageView.centerImageInRect(targetRect: Rect) {
    resetZoom()

    val imageRect = drawable.bounds
    val boundsF = RectF(imageRect)
    imageMatrix.mapRect(boundsF)
    boundsF.round(imageRect)

    // Only operate if image is larger than targetRect
    if (imageRect.width() > targetRect.width() || imageRect.height() > targetRect.height()) {

        // Determine scale factor to fit image in targetRect
        val zoomScaleX = targetRect.width().toFloat() / imageRect.width()
        val zoomScaleY = targetRect.height().toFloat() / imageRect.height()
        val scale =  min(zoomScaleX, zoomScaleY)

        // Determine translation scale to center image in targetRect
        val translationScaleX = targetRect.centerX().toFloat()/imageRect.centerX()
        val translationScaleY = targetRect.centerY().toFloat()/imageRect.centerY()

        val focusX = 0.5f*translationScaleX
        val focusY = 0.5f*translationScaleY

        // Apply scale factor and translation to center image in targetRect
        setZoom(scale, focusX, focusY)
    }
}
