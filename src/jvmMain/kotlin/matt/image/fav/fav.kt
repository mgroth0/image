package matt.image.fav

import matt.lang.anno.SeeURL
import matt.lang.fnf.runCatchingFileTrulyNotFound
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URL
import java.util.UnknownFormatConversionException
import javax.imageio.ImageIO


fun tryToLoadImageStreamAndTakeLargestImage(
    url: URL,
    onMinorException: (e: Exception) -> Unit
): BufferedImage? {
    val stream =
        runCatchingFileTrulyNotFound(
            file = { error("how to get file from $url?") }
        ) {
            url.openStream()
        }.getOrElse {
            onMinorException(it as Exception)
            @SeeURL("https://youtrack.jetbrains.com/issue/KT-1436/Support-non-local-break-and-continue")
            null
        } ?: return null

    val ims =
        try {
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
                    val image =
                        read(
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
