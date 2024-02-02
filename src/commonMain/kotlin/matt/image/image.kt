package matt.image

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import matt.lang.anno.SeeURLs
import matt.lang.mime.BinaryMimeData
import matt.lang.mime.BinaryRepresentableData
import matt.lang.mime.CachingTextData
import matt.lang.mime.TextMimeData
import matt.lang.model.value.MyValueClass
import matt.lang.model.value.MyValueClassSerializer

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
    override fun toPng(): Png = this

    override fun toJpeg(): Jpeg = pngToJpeg(this)

    override val mimeType get() = matt.lang.model.file.types.Png.mimeType


    override val asBinary: ByteArray
        get() = bytes


}

internal expect fun pngToJpeg(png: Png): Jpeg

@Serializable
class Jpeg(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster {
    override fun toJpeg(): Jpeg = this

    override fun toPng(): Png = jpegToPng(this)
}

internal expect fun jpegToPng(jpeg: Jpeg): Png


@Serializable
class Argb(
    val pixels: IntArray,
    val width: Int
) : ImmutableRaster {
    override fun toJpeg(): Jpeg = argbToJpeg(this)

    override fun toPng(): Png = argbToPng(this)

    val derivedHeight by lazy {
        val pixSize = pixels.size
        check(pixSize % width == 0)
        pixSize / width
    }

}

internal expect fun argbToJpeg(argb: Argb): Jpeg
internal expect fun argbToPng(argb: Argb): Png


interface VectorGraphic : Image


@Serializable(with = Svg.Serializer::class)
class Svg(val code: String) : MyValueClass<String>(code.toString()),
    BinaryRepresentableData by CachingTextData(code.toString()),
    TextMimeData,
    VectorGraphic {
    constructor(codeSequence: CharSequence) : this(codeSequence.toString())

    companion object Serializer : MyValueClassSerializer<String, Svg>(serializer<String>()) {
        override fun construct(value: String) = Svg(value)
    }

    override val mimeType = matt.lang.model.file.types.Png.mimeType.copy(subType = "svg", suffix = "xml")
    override val asText = code
}


