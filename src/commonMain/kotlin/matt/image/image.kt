package matt.image

import kotlinx.serialization.Serializable
import matt.lang.anno.SeeURLs
import matt.lang.mime.BinaryCachingTextMimeData
import matt.lang.mime.BinaryMimeData


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
class Png(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster, BinaryMimeData {
    override fun toPng(): Png {
        return this
    }

    override fun toJpeg(): Jpeg {
        return pngToJpeg(this)
    }

    override val mimeType: String get() = matt.lang.model.file.types.Png.mimeType


    override val data: ByteArray
        get() = bytes


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
class Svg(val code: CharSequence) : BinaryCachingTextMimeData(), VectorGraphic {
    override val mimeType = "image/svg+xml"
    override val textData by lazy { code.toString() }
}


