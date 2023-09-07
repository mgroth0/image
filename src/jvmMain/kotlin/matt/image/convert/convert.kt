package matt.image.convert

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
    ImageIO.write(this, "png", stream)
    return Png(stream.toByteArray())
}
