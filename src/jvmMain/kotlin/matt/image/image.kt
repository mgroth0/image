@file:JavaIoFileIsOk
@file:JvmName("ImageJvmKt")

package matt.image

import matt.image.convert.toPng
import matt.lang.anno.ok.JavaIoFileIsOk
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


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
fun Png.toBufferedImage() = bytes.readImage()

private fun ByteArray.readImage() = inputStream().readImage()

private fun InputStream.readImage() = ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull()
private fun File.readImage() = ImageIO.read(this) ?: throwExplainingImageIoReadReturningNull()
private fun throwExplainingImageIoReadReturningNull(): Nothing =
    error("no registered ImageReader claims to be able to read this")