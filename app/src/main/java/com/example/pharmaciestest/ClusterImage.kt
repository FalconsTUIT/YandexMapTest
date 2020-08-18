package com.example.pharmaciestest

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.util.DisplayMetrics
import com.yandex.runtime.image.ImageProvider
import kotlin.math.abs
import kotlin.math.sqrt


/**
 * Created by Mikle Kovalev on 18.08.2020 13:04.
 */

class ClusterImage(private val text: String, private val context: Context) : ImageProvider() {
    override fun getId(): String {
        return "text_$text"
    }

    override fun getImage(): Bitmap {
        val metrics = DisplayMetrics()
        val manager = (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)

        val textPaint = Paint()
        textPaint.textSize = 15 * metrics.density
        textPaint.textAlign = Align.CENTER
        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true

        val widthF: Float = textPaint.measureText(text)
        val textMetrics: Paint.FontMetrics = textPaint.fontMetrics
        val heightF: Float = abs(textMetrics.bottom) + abs(textMetrics.top)
        val textRadius = sqrt(widthF * widthF + heightF * heightF.toDouble()).toFloat() / 2
        val internalRadius: Float = textRadius + 3 * metrics.density
        val externalRadius: Float = internalRadius + 3 * metrics.density

        val width: Float = 2 * externalRadius + 0.5f

        val bitmap = Bitmap.createBitmap(width.toInt(), width.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint()
        backgroundPaint.isAntiAlias = true
        backgroundPaint.color = Color.RED
        canvas.drawCircle(width / 2, width / 2, externalRadius, backgroundPaint)

        backgroundPaint.color = Color.WHITE
        canvas.drawCircle(width / 2, width / 2, internalRadius, backgroundPaint)

        canvas.drawText(
            text,
            width / 2,
            width / 2 - (textMetrics.ascent + textMetrics.descent) / 2,
            textPaint
        )

        return bitmap
    }

}