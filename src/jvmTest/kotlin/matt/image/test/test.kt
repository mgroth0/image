package matt.image.test


import matt.image.convert.ImageIoFormats
import matt.image.convert.toJPeg
import matt.image.convert.toPng
import matt.image.icon.ICON_SIZES
import matt.image.immutable.ImmutableByteArray
import matt.test.scaffold.TestScaffold
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_BYTE_BINARY

class ImageTests : TestScaffold() {

    override fun initEnums() {
    }

    override fun initObjects() {
        ImageIoFormats
    }

    override fun initVals() = assertRunsInOneMinute {
        ICON_SIZES
    }

    override fun instantiateClasses() {
        ImmutableByteArray(byteArrayOf())
    }

    override fun runFunctions() {
        val im = BufferedImage(1, 1, TYPE_BYTE_BINARY)
        im.toPng()
        im.toJPeg()
    }


}
