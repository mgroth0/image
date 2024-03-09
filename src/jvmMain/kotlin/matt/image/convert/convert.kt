package matt.image.convert

import matt.image.common.Jpeg
import matt.image.common.Png
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


fun jswingIconToImage(jswingIcon: javax.swing.Icon): BufferedImage {
    val bufferedImage =
        BufferedImage(
            jswingIcon.iconWidth, jswingIcon.iconHeight,
            BufferedImage.TYPE_INT_ARGB
        )
    jswingIcon.paintIcon(null, bufferedImage.graphics, 0, 0)
    return bufferedImage
}


object ImageIoFormats {
    const val PNG = "png"
    const val JPG = "jpg"
}


fun BufferedImage.toPng(): Png {
    val stream = ByteArrayOutputStream()
    check(ImageIO.write(this, ImageIoFormats.PNG, stream)) {
        "could not find writer. Available: ${ImageIO.getWriterFormatNames().toList().joinToString { it }}"
    }
    return Png(stream.toByteArray())
}


fun BufferedImage.toJPeg(): Jpeg {



    check(!colorModel.hasAlpha()) {
        "I think JPEG cannot encode alpha"
    }
    /*check(JPEGImageWriterSpi().canEncodeImage(this)) {
        "JPEGImageWriterSpi cannot encode this"
    }*/
    val stream = ByteArrayOutputStream()
    check(ImageIO.write(this, ImageIoFormats.JPG, stream)) {
        "could not find writer. Available: ${ImageIO.getWriterFormatNames().toList().joinToString { it }}"
    }
    val jpegBytes = stream.toByteArray()
    return Jpeg(jpegBytes)
}

