package matt.image

import matt.image.a.toBitmap
import matt.image.a.toJpeg
import matt.image.a.toPng
import matt.image.common.Argb
import matt.image.common.Jpeg
import matt.image.common.Png
import matt.image.common.WebP
import matt.lang.anno.ProbablyCanOptimizeWayMore


@ProbablyCanOptimizeWayMore
actual fun pngToJpeg(png: Png) = png.toBitmap().toJpeg()

@ProbablyCanOptimizeWayMore
actual fun jpegToPng(jpeg: Jpeg) = jpeg.toBitmap().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToPng(argb: Argb) = argb.toBitmap().toPng()

@ProbablyCanOptimizeWayMore
actual fun argbToJpeg(argb: Argb): Jpeg = TODO()

@ProbablyCanOptimizeWayMore
actual fun webPToJpeg(webP: WebP): Jpeg = webP.toBitmap().toJpeg()

@ProbablyCanOptimizeWayMore
actual fun webPToPng(webP: WebP): Png = webP.toBitmap().toPng()


