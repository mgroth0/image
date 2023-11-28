package matt.image.convert

import matt.image.Jpeg
import matt.image.Png
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


fun jswingIconToImage(jswingIcon: javax.swing.Icon): BufferedImage {
    val bufferedImage = BufferedImage(
        jswingIcon.iconWidth, jswingIcon.iconHeight,
        BufferedImage.TYPE_INT_ARGB
    )
    jswingIcon.paintIcon(null, bufferedImage.graphics, 0, 0)
    return bufferedImage
}


fun BufferedImage.toPng(): Png {
    val stream = ByteArrayOutputStream()
    check(ImageIO.write(this, "png", stream)) {
        "could not find writer. Available: ${ImageIO.getWriterFormatNames().toList().joinToString { it }}"
    }
    return Png(stream.toByteArray())
}


fun BufferedImage.toJPeg(): Jpeg {
    check(!this.colorModel.hasAlpha()) {
        "I think JPEG cannot encode alpha"
    }
    /*check(JPEGImageWriterSpi().canEncodeImage(this)) {
        "JPEGImageWriterSpi cannot encode this"
    }*/
    val stream = ByteArrayOutputStream()
    check(ImageIO.write(this, "jpg", stream)) {
        "could not find writer. Available: ${ImageIO.getWriterFormatNames().toList().joinToString { it }}"
    }
    val jpegBytes = stream.toByteArray()
    println("jpegBytes.size=${jpegBytes.size}")
    return Jpeg(jpegBytes)
}
