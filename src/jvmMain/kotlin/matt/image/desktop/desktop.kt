@file:JavaIoFileIsOk
package matt.image.desktop

import matt.image.common.Argb
import matt.image.common.Image
import matt.image.common.ImmutableRaster
import matt.image.common.Jpeg
import matt.image.common.Png
import matt.image.common.Raster
import matt.image.common.WebP
import matt.image.convert.toPng
import matt.lang.anno.SupportedByChatGPT
import matt.lang.anno.ok.JavaIoFileIsOk
import matt.lang.file.toJFile
import matt.lang.fnf.runCatchingTrulyNotFound
import matt.lang.model.file.FsFile
import matt.lang.model.file.types.RasterImage
import matt.lang.model.file.types.TypedFile
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


fun TypedFile<RasterImage, *>.readWidth() = ImageIO.read(toJFile()).width

fun TypedFile<RasterImage, *>.readWidthOrNullIfDoesNotExist() =
    runCatchingTrulyNotFound {
        ImageIO.read(toJFile()).width
    }.getOrNull()



fun ByteArray.readImage() = inputStream().readImage()


fun FsFile.loadImage() = toJFile().loadImage()
fun File.loadImage() = readImage()



fun ImmutableRaster.toBufferedImage() =
    when (this) {
        is Png  -> bytes.readImage()
        is Jpeg -> bytes.readImage()
        is WebP -> bytes.readImage()
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



interface JDesktopRaster : Raster {
    fun toBufferedImage(): BufferedImage
}


@JvmInline
value class JBufferedImage(val bImg: BufferedImage) : Image {
    fun toPng() = bImg.toPng()
}

@JvmInline
value class JSwingIcon(val icon: javax.swing.Icon) : Image

fun BufferedImage.save(file: FsFile) = save(file.toJFile())
fun BufferedImage.save(file: File) {
    ImageIO.write(this, file.extension, file)
}

fun ByteArray.isValidImage(): Boolean {
    try {
        inputStream().readImage()
    } catch (e: IOException) {
        return false
    }
    return true
}



private fun InputStream.readImage() =
    ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull("this image input stream")

private fun File.readImage() = ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull(path)
private fun throwExplainingImageIoReadReturningNull(image: String): Nothing =
    error("no registered ImageReader claims to be able to read $image")


