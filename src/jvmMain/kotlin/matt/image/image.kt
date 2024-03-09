@file:JavaIoFileIsOk

package matt.image

import matt.image.common.Argb
import matt.image.common.Jpeg
import matt.image.common.Png
import matt.image.common.WebP
import matt.image.convert.toJPeg
import matt.image.convert.toPng
import matt.image.desktop.toBufferedImage
import matt.lang.anno.ProbablyCanOptimizeWayMore
import matt.lang.anno.ok.JavaIoFileIsOk


@ProbablyCanOptimizeWayMore
actual fun pngToJpeg(png: Png) = png.toBufferedImage().toJPeg()

@ProbablyCanOptimizeWayMore
actual fun jpegToPng(jpeg: Jpeg) = jpeg.toBufferedImage().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToPng(argb: Argb) = argb.toBufferedImage().toPng()

@ProbablyCanOptimizeWayMore
actual fun webPToJpeg(webP: WebP) = webP.toBufferedImage().toJPeg()

@ProbablyCanOptimizeWayMore
actual fun webPToPng(webP: WebP) = webP.toBufferedImage().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToJpeg(argb: Argb) = argb.toBufferedImage().toJPeg()



