package com.example.pharmaciestest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import androidx.annotation.DrawableRes
import com.yandex.runtime.image.ImageProvider
import java.util.UUID

class PharmacyPlaceMark(
    private val context: Context,
    placeMarkColor: PlaceMarkColors
) : ImageProvider() {

    companion object {
        private const val PLACE_MARK_WIDTH_PERCENTS: Double = 0.075
    }

    private val image: Bitmap

    init {
        image = when (placeMarkColor) {
            PlaceMarkColors.BLUE -> {
                imageFromResource(R.drawable.ic_pin_filter_blue)
            }
            PlaceMarkColors.GRAY -> {
                imageFromResource(R.drawable.ic_pin_filter_gray)
            }
        }
    }

    override fun getId(): String {
        return UUID.randomUUID().toString()
    }

    override fun getImage(): Bitmap = image

    private fun imageFromResource(@DrawableRes drawableId: Int): Bitmap {
        val resource = BitmapFactory.decodeResource(context.resources, drawableId)
        context.display?.let { display ->
            val displaySize = Point()
            display.getRealSize(displaySize)
            val displayWidth = displaySize.y
            val imageAspectRatio = resource.width * 1.0 / resource.height
            val imageWidth = displayWidth * PLACE_MARK_WIDTH_PERCENTS
            val imageHeight = imageWidth / imageAspectRatio
            return Bitmap.createBitmap(resource, 0, 0, imageWidth.toInt(), imageHeight.toInt())
        }
        return resource
    }

    enum class PlaceMarkColors {
        GRAY, BLUE
    }
}