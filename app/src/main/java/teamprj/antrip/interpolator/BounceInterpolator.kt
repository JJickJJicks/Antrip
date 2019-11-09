package com.test.ui_practice.interpolator

import android.view.animation.Interpolator
import kotlin.math.cos
import kotlin.math.pow

class BounceInterpolator : Interpolator {
    private var mAmplitude = 1.0
    private var mFrequency = 10.0

    constructor(mAmplitude: Double, mFrequency: Double) {
        this.mAmplitude = mAmplitude
        this.mFrequency = mFrequency
    }

    override fun getInterpolation(time: Float): Float {
        return  (-1 * Math.E.pow(-time / mAmplitude) * cos(mFrequency * time) + 1).toFloat()
    }
}