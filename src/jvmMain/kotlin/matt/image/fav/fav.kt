package matt.image.fav

import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO


fun tryToLoadImageStreamAndTakeLargestImage(
    url: URL,
    onMinorException: (e: Exception) -> Unit
): BufferedImage? {
    val stream = try {
        url.openStream()
    } catch (e: FileNotFoundException) {
        onMinorException(e)
        return null
    }

    val ims = try {
        val imStream = ImageIO.createImageInputStream(stream)
        val readerIterator = ImageIO.getImageReaders(imStream)
        if (!readerIterator.hasNext()) {
            onMinorException(Exception("READER ITERATOR HAS NO NEXT"))
            return null
        }
        readerIterator.next().run {
            input = imStream
            val images = mutableListOf<BufferedImage>()
            for (i in 0..<getNumImages(true)) {
                val image = this.read(
                    i,
                    null
                )
                images += image
            }
            images
        }
    } catch (e: IOException) {
        onMinorException(e)
        return null
    } finally {
        stream.close()
    }
    val im = ims.maxBy { it.height }
    return try {
        im
    } catch (ex: UnsupportedEncodingException) {
        onMinorException(ex)
        null
    } catch (ex: UnknownFormatConversionException) {
        onMinorException(ex)
        null
    }
}