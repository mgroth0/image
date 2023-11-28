package matt.image

import kotlinx.serialization.Serializable
import matt.lang.anno.SeeURLs
import matt.lang.mime.MimeData


interface Image
interface ImmutableImage : Image
interface Raster : Image {
    fun toPng(): Png
    fun toJpeg(): Jpeg
}

sealed interface ImmutableRaster : Raster, ImmutableImage

sealed interface ByteBasedRaster : ImmutableRaster {
    val bytes: ByteArray
}


@SeeURLs(
    "https://developer.android.com/guide/topics/media/platform/supported-formats#image-formats",
    "https://stackoverflow.com/questions/24333450/formats-supported-by-bitmapfactory-decodebytearray"
)
sealed interface NativeAndroidDecodableRaster : ByteBasedRaster

/*
    this works with any encoded raster image in a format supported by Skia (BMP, GIF, HEIF, ICO, JPEG, PNG, WBMP, WebP)

    https://rust-skia.github.io/doc/skia_safe/type.Image.html
*/
sealed interface NativeSkiaDecodableRaster : ByteBasedRaster

sealed interface NativeAndroidAndSkiaDecodableRaster : ByteBasedRaster

@Serializable
class Png(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster {
    override fun toPng(): Png {
        return this
    }

    override fun toJpeg(): Jpeg {
        return pngToJpeg(this)
    }
}

internal expect fun pngToJpeg(png: Png): Jpeg

@Serializable
class Jpeg(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster {
    override fun toJpeg(): Jpeg {
        return this
    }

    override fun toPng(): Png {
        return jpegToPng(this)
    }
}

internal expect fun jpegToPng(jpeg: Jpeg): Png


@Serializable
class Argb(
    val pixels: IntArray,
    val width: Int
) : ImmutableRaster {
    override fun toJpeg(): Jpeg {
        return argbToJpeg(this)
    }

    override fun toPng(): Png {
        return argbToPng(this)
    }

    val derivedHeight by lazy {
        val pixSize = pixels.size
        check(pixSize % width == 0)
        pixSize / width
    }

}

internal expect fun argbToJpeg(argb: Argb): Jpeg
internal expect fun argbToPng(argb: Argb): Png


interface VectorGraphic : Image


@Serializable
class Svg(val code: CharSequence) : VectorGraphic, MimeData {
    override val mimeType = "image/svg+xml"
    override val data by lazy {
        code.toString()
    }
}


