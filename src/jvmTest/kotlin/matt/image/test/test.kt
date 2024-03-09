package matt.image.test


import matt.image.convert.ImageIoFormats
import matt.image.convert.toJPeg
import matt.image.convert.toPng
import matt.image.icon.ICON_SIZES
import matt.test.Tests
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_BINARY
import kotlin.test.Test

class ImageTests : Tests() {

    @Test fun initObjects() {
        ImageIoFormats
    }

    @Test fun initVals() =
        assertRunsInOneMinute {
            ICON_SIZES
        }

    @Test fun runFunctions() {
        val im = BufferedImage(1, 1, TYPE_BYTE_BINARY)
        im.toPng()
        im.toJPeg()
    }
}
