package com.squidink.xkcdviewer.extensions

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import android.widget.ImageView

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