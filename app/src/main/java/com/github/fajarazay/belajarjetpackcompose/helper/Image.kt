package com.github.fajarazay.belajarjetpackcompose.helper

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.core.Draw
import androidx.ui.foundation.DrawImage
import androidx.ui.graphics.Image
import androidx.ui.graphics.ImageConfig
import androidx.ui.graphics.NativeImage
import androidx.ui.graphics.colorspace.ColorSpace
import androidx.ui.graphics.colorspace.ColorSpaces
import androidx.ui.layout.Container
import com.squareup.picasso.Picasso

/**
 * Created by FAJAR SEPTIAN on 28/02/20.
 *
 * @Author Fajar Septian
 * @Email fajarajay10@gmail.com
 * @Github https://github.com/fajarazay
 */
// Taken from https://gist.github.com/nglauber/8a3c39db47813483d788fb2914f0f21f

@Composable
fun Image(url: String, width: Dp, height: Dp) {
    var image by +state<Image?> { null }
    var drawable by +state<Drawable?> { null }
    +onCommit(url) {

        val picasso = Picasso.get()

        val target = object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.let {
                    AndroidImage(it)
                }
            }
        }

        picasso
            .load(url)
            .into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }
    //width?
    image?.let { DrawImage(it) }
    Container(height = height, width = width) {
        val theImage = image
        val theDrawable = drawable
        if (theImage != null) {
            DrawImage(image = theImage)
        } else if (theDrawable != null) {
            Draw { canvas, parentSize -> theDrawable.draw(canvas.nativeCanvas) }
        }
    }
}

internal class AndroidImage(private val bitmap: Bitmap) : Image {

    /**
     * @see Image.width
     */
    override val width: Int
        get() = bitmap.width

    /**
     * @see Image.height
     */
    override val height: Int
        get() = bitmap.height

    override val config: ImageConfig
        get() = bitmap.config.toImageConfig()

    /**
     * @see Image.colorSpace
     */
    override val colorSpace: ColorSpace
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bitmap.colorSpace?.toComposeColorSpace() ?: ColorSpaces.Srgb
        } else {
            ColorSpaces.Srgb
        }

    /**
     * @see Image.hasAlpha
     */
    override val hasAlpha: Boolean
        get() = bitmap.hasAlpha()

    /**
     * @see Image.nativeImage
     */
    override val nativeImage: NativeImage
        get() = bitmap

    /**
     * @see
     */
    override fun prepareToDraw() {
        bitmap.prepareToDraw()
    }
}

internal fun Bitmap.Config.toImageConfig(): ImageConfig {
    // Cannot utilize when statements with enums that may have different sets of supported
    // values between the compiled SDK and the platform version of the device.
    // As a workaround use if/else statements
    // See https://youtrack.jetbrains.com/issue/KT-30473 for details
    @Suppress("DEPRECATION")
    return if (this == Bitmap.Config.ALPHA_8) {
        ImageConfig.Alpha8
    } else if (this == Bitmap.Config.RGB_565) {
        ImageConfig.Rgb565
    } else if (this == Bitmap.Config.ARGB_4444) {
        ImageConfig.Argb8888 // Always upgrade to Argb_8888
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == Bitmap.Config.RGBA_F16) {
        ImageConfig.F16
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == Bitmap.Config.HARDWARE) {
        ImageConfig.Gpu
    } else {
        ImageConfig.Argb8888
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun ColorSpace.toFrameworkColorSpace(): android.graphics.ColorSpace {
    val frameworkNamedSpace = when (this) {
        ColorSpaces.Srgb -> android.graphics.ColorSpace.Named.SRGB
        ColorSpaces.Aces -> android.graphics.ColorSpace.Named.ACES
        ColorSpaces.Acescg -> android.graphics.ColorSpace.Named.ACESCG
        ColorSpaces.AdobeRgb -> android.graphics.ColorSpace.Named.ADOBE_RGB
        ColorSpaces.Bt2020 -> android.graphics.ColorSpace.Named.BT2020
        ColorSpaces.Bt709 -> android.graphics.ColorSpace.Named.BT709
        ColorSpaces.CieLab -> android.graphics.ColorSpace.Named.CIE_LAB
        ColorSpaces.CieXyz -> android.graphics.ColorSpace.Named.CIE_XYZ
        ColorSpaces.DciP3 -> android.graphics.ColorSpace.Named.DCI_P3
        ColorSpaces.DisplayP3 -> android.graphics.ColorSpace.Named.DISPLAY_P3
        ColorSpaces.ExtendedSrgb -> android.graphics.ColorSpace.Named.EXTENDED_SRGB
        ColorSpaces.LinearExtendedSrgb ->
            android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB
        ColorSpaces.LinearSrgb -> android.graphics.ColorSpace.Named.LINEAR_SRGB
        ColorSpaces.Ntsc1953 -> android.graphics.ColorSpace.Named.NTSC_1953
        ColorSpaces.ProPhotoRgb -> android.graphics.ColorSpace.Named.PRO_PHOTO_RGB
        ColorSpaces.SmpteC -> android.graphics.ColorSpace.Named.SMPTE_C
        else -> android.graphics.ColorSpace.Named.SRGB
    }
    return android.graphics.ColorSpace.get(frameworkNamedSpace)
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun android.graphics.ColorSpace.toComposeColorSpace(): ColorSpace {
    return when (this) {
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB)
        -> ColorSpaces.Srgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ACES)
        -> ColorSpaces.Aces
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ACESCG)
        -> ColorSpaces.Acescg
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ADOBE_RGB)
        -> ColorSpaces.AdobeRgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.BT2020)
        -> ColorSpaces.Bt2020
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.BT709)
        -> ColorSpaces.Bt709
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.CIE_LAB)
        -> ColorSpaces.CieLab
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.CIE_XYZ)
        -> ColorSpaces.CieXyz
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.DCI_P3)
        -> ColorSpaces.DciP3
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.DISPLAY_P3)
        -> ColorSpaces.DisplayP3
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.EXTENDED_SRGB)
        -> ColorSpaces.ExtendedSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB)
        -> ColorSpaces.LinearExtendedSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.LINEAR_SRGB)
        -> ColorSpaces.LinearSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.NTSC_1953)
        -> ColorSpaces.Ntsc1953
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.PRO_PHOTO_RGB)
        -> ColorSpaces.ProPhotoRgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SMPTE_C)
        -> ColorSpaces.SmpteC
        else -> ColorSpaces.Srgb
    }
}