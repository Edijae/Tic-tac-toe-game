package com.samuel.tictac.utilisimport

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.samuel.tictac.R
import kotlin.math.roundToInt

private var scaleAnimation: Animation? = null

class AppUtils {

    companion object {
        private var previousTimeMillis = System.currentTimeMillis()
        private var counter = 0L

        @Synchronized
        fun generateID(): Long {
            val currentTimeMillis = System.currentTimeMillis()
            counter = if (currentTimeMillis == previousTimeMillis) counter + 1L and 1048575L else 0L
            previousTimeMillis = currentTimeMillis
            val timeComponent = currentTimeMillis and 8796093022207L shl 20
            return timeComponent or counter
        }

        fun getScreenHeight(context: Context): Int {
            var statusBarHeight = 0
            val resourceId: Int = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = (context.resources
                    .getDimensionPixelSize(resourceId) / context.resources.displayMetrics.density).roundToInt()
            }
            return context.resources.displayMetrics.heightPixels - statusBarHeight
        }
    }
}

fun View.scale() {
    if (scaleAnimation == null) {
        scaleAnimation = AnimationUtils.loadAnimation(
            context, R.anim.scale_animation
        )
    }
    startAnimation(scaleAnimation)
}