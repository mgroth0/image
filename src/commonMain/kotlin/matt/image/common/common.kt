package matt.image.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import matt.image.argbToJpeg
import matt.image.argbToPng
import matt.image.common.Svg.Serializer
import matt.image.jpegToPng
import matt.image.pngToJpeg
import matt.image.webPToJpeg
import matt.image.webPToPng
import matt.lang.anno.SeeURLs
import matt.lang.mime.BinaryMimeData
import matt.lang.mime.BinaryRepresentableData
import matt.lang.mime.CachingTextData
import matt.lang.mime.MimeTypes
import matt.lang.mime.TextMimeData
import matt.lang.model.value.MyValueClass
import matt.model.data.rect.IntRectSize
import matt.model.data.value.MyValueClassSerializer


interface VectorGraphic : Image

@Serializable(with = Serializer::class)
class Svg(val code: String) :
    MyValueClass<String>(code.toString()),
    BinaryRepresentableData by CachingTextData(code.toString()),
    TextMimeData,
    VectorGraphic {
    constructor(codeSequence: CharSequence) : this(codeSequence.toString())


    companion object Serializer : MyValueClassSerializer<String, Svg>(serializer<String>()) {
        override fun construct(value: String) = Svg(value)
    }


    override val mimeType = MimeTypes.PNG.copy(subType = "svg", suffix = "xml")
    override val asText = code
}



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




/*no transparency!*/
@Serializable
class Jpeg(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster {
    override fun toJpeg(): Jpeg = this

    override fun toPng(): Png = jpegToPng(this)
}


/*apparently more compressed than JPEG, and offers transparency which JPEG does not!*/
@Serializable
class WebP(override val bytes: ByteArray) : NativeAndroidAndSkiaDecodableRaster {
    override fun toPng(): Png = webPToPng(this)

    override fun toJpeg(): Jpeg = webPToJpeg(this)
}


interface Image
interface ImmutableImage : Image
interface Raster : Image {
    fun toPng(): Png
    fun toJpeg(): Jpeg
}

sealed interface ImmutableRaster : Raster, ImmutableImage
interface Rasterizable {
    fun rasterize(size: IntRectSize): ImmutableRaster
}

interface PngRasterizable: Rasterizable {
    override fun rasterize(size: IntRectSize): Png
}

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

    override val mimeType get() = MimeTypes.PNG


    override val asBinary: ByteArray
        get() = bytes
}
