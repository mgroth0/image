package matt.image.mutate

import matt.color.IntColor
import matt.color.toAwtColor
import matt.image.Jpeg
import matt.image.convert.toJPeg
import matt.image.toBufferedImage
import matt.lang.evenodd.isEven
import matt.lang.jpy.ExcludeFromPython
import matt.lang.jpy.PyClass
import matt.lang.model.file.AnyFsFile
import java.awt.image.BufferedImage
import kotlin.math.roundToInt


fun BufferedImage.toSquare() = Square.transform(this)

fun BufferedImage.resize(
    h: Int,
    w: Int
) = Resize(h = h, w = w).transform(this)

fun Jpeg.downsampleKeepingAr(
    desiredWidth: Int,
    allowAlreadyLower: Boolean
) = toBufferedImage().downsampleKeepingAr(desiredWidth, allowAlreadyLower = allowAlreadyLower).toJPeg()

fun BufferedImage.downsampleKeepingAr(
    desiredWidth: Int,
    allowAlreadyLower: Boolean
) = DownsampleKeepingAr(desiredWidth = desiredWidth, allowAlreadyLower = allowAlreadyLower).transform(this)


fun AnyFsFile.noTransform() = TransformingImage(this)
fun AnyFsFile.withTransform(transform: ImageTransform?) = TransformingImage(this, transform)
class TransformingImage(
    val file: AnyFsFile,
    val transform: ImageTransform? = null
)

@PyClass
interface ImageTransform {
    @ExcludeFromPython
    fun transform(input: BufferedImage): BufferedImage
}

@PyClass
data class Transforms(val steps: List<ImageTransform>) : ImageTransform {
    constructor(vararg steps: ImageTransform) : this(steps.toList())

    override fun transform(input: BufferedImage): BufferedImage {
        var output = input
        steps.forEach {
            output = it.transform(output)
        }
        return output
    }
}


@PyClass
data class DownsampleKeepingAr(
    private val desiredWidth: Int,
    val allowAlreadyLower: Boolean
) : ImageTransform {
    override fun transform(input: BufferedImage): BufferedImage = input.downsampleKeepingAr()

    private fun BufferedImage.downsampleKeepingAr(): BufferedImage = when {
        width < desiredWidth  -> {
            if (!allowAlreadyLower) error("Current width ($width) is less than desired width ($desiredWidth).")
            this
        }

        width == desiredWidth -> this
        else                  -> {
            val currentWidthDouble = width.toDouble()
            val wPercent = desiredWidth / currentWidthDouble
            val hSize = (height * wPercent).toInt()
            val imgResized = resize(w = desiredWidth, h = hSize)
            imgResized
        }
    }
}

@PyClass
data class CenteredProportionalResize(
    private val h: Float,
    private val w: Float
) : ImageTransform {
    override fun transform(input: BufferedImage): BufferedImage = input.resize()

    private fun BufferedImage.resize(): BufferedImage {
        if (h == 1f && w == 1f) return this
        check(h in 0f..1f)
        check(w in 0f..1f)
        val outputHeight = (h * height).roundToInt()
        val outputWidth = (w * width).roundToInt()
        check(outputHeight > 0)
        check(outputWidth > 0)
        val cutOutHeight = height - outputHeight
        val cutOutWidth = width - outputWidth
        val cutOutHeightTop = if (cutOutHeight.isEven()) cutOutHeight / 2
        else (cutOutHeight - 1) / 2
        val cutOutWidthLeft = if (cutOutWidth.isEven()) cutOutWidth / 2
        else (cutOutWidth - 1) / 2
        return getSubimage(cutOutWidthLeft, cutOutHeightTop, outputWidth, outputHeight)
    }
}

@PyClass
data class Resize(
    private val h: Int,
    private val w: Int
) : ImageTransform {
    override fun transform(input: BufferedImage): BufferedImage = input.resize()

    private fun BufferedImage.resize(): BufferedImage = BufferedImage(w, h, this.type).also {
        val g = it.createGraphics()
        g.drawImage(this, 0, 0, w, h, null)
        g.dispose()
    }
}


@PyClass
data class Occlude(
    private val x: Int,
    private val y: Int,
    private val w: Int,
    private val h: Int,
    private val color: IntColor
) : ImageTransform {
    override fun transform(input: BufferedImage): BufferedImage {

        val output = BufferedImage(input.width, input.height, input.type)

        val g2d = output.createGraphics()
        g2d.drawImage(input, 0, 0, null)
        g2d.color = color.toAwtColor()
        g2d.fillRect(x, y, w, h)
        g2d.dispose()
        return output

    }


}


@PyClass
data object Square : ImageTransform {
    override fun transform(input: BufferedImage): BufferedImage = input.toSquare()

    private fun BufferedImage.toSquare(): BufferedImage {
        if (height == width) return this
        if (height > width) {
            var diff = height - width
            val even = diff % 2 == 0
            if (!even) {
                diff += 1
            }
            val diffPerSide = diff / 2
            return if (even) {
                getSubimage(0, diffPerSide, width, height - diffPerSide)
            } else {
                getSubimage(1, diffPerSide, width - 1, height - diffPerSide)
            }
        } else {
            var diff = width - height
            val even = diff % 2 == 0
            if (!even) {
                diff += 1
            }
            val diffPerSide = diff / 2
            return if (even) {
                getSubimage(diffPerSide, 0, width - diffPerSide, height)
            } else {
                getSubimage(diffPerSide, 1, width - diffPerSide, height - 1)
            }
        }
    }
}
