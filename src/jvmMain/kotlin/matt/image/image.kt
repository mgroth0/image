@file:JavaIoFileIsOk
@file:JvmName("ImageJvmKt")

package matt.image

import matt.image.convert.toJPeg
import matt.image.convert.toPng
import matt.lang.anno.ProbablyCanOptimizeWayMore
import matt.lang.anno.SupportedByChatGPT
import matt.lang.anno.ok.JavaIoFileIsOk
import matt.lang.file.toJFile
import matt.lang.model.file.types.RasterImage
import matt.lang.model.file.types.TypedFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


interface JDesktopRaster : Raster {
    fun toBufferedImage(): BufferedImage
}


@JvmInline
value class JBufferedImage(val bImg: BufferedImage) : Image {
    fun toPng() = bImg.toPng()
}

@JvmInline
value class JSwingIcon(val icon: javax.swing.Icon) : Image


fun BufferedImage.save(file: File): File {
    ImageIO.write(this, file.extension, file)
    return file
}

fun ByteArray.isValidImage(): Boolean {
    try {
        inputStream().readImage()
    } catch (e: IOException) {
        return false
    }
    return true
}

fun File.loadImage() = this.readImage()


fun ImmutableRaster.toBufferedImage() = when (this) {
    is Png  -> bytes.readImage()
    is Jpeg -> bytes.readImage()
    is Argb -> {
        @SupportedByChatGPT
        val bufferedImage = BufferedImage(width, pixels.size / width, BufferedImage.TYPE_INT_ARGB)

        pixels.forEachIndexed { index, pixel ->
            val x = index % width
            val y = index / width
            bufferedImage.setRGB(x, y, pixel)
        }

        bufferedImage
    }
}

@ProbablyCanOptimizeWayMore
actual fun pngToJpeg(png: Png) = png.toBufferedImage().toJPeg()

@ProbablyCanOptimizeWayMore
actual fun jpegToPng(jpeg: Jpeg) = jpeg.toBufferedImage().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToPng(argb: Argb) = argb.toBufferedImage().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToJpeg(argb: Argb) = argb.toBufferedImage().toJPeg()


fun ByteArray.readImage() = inputStream().readImage()

private fun InputStream.readImage() =
    ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull("this image input stream")

private fun File.readImage() = ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull(path)
private fun throwExplainingImageIoReadReturningNull(image: String): Nothing =
    error("no registered ImageReader claims to be able to read $image")


fun TypedFile<RasterImage>.readWidth() = ImageIO.read(this.toJFile()).width

fun TypedFile<RasterImage>.readWidthOrNullIfDoesNotExist() = try {
    ImageIO.read(this.toJFile()).width
} catch (e: FileNotFoundException) {
    null
}

