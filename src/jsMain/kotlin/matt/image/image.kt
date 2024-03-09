package matt.image

import matt.image.common.Argb
import matt.image.common.Jpeg
import matt.image.common.Png
import matt.image.common.WebP
import matt.lang.anno.ProbablyCanOptimizeWayMore

@ProbablyCanOptimizeWayMore
actual fun pngToJpeg(png: Png): Jpeg = TODO()

@ProbablyCanOptimizeWayMore
actual fun jpegToPng(jpeg: Jpeg): Png = TODO()

@ProbablyCanOptimizeWayMore
actual fun argbToPng(argb: Argb): Png = TODO()

@ProbablyCanOptimizeWayMore
actual fun argbToJpeg(argb: Argb): Jpeg = TODO()


@ProbablyCanOptimizeWayMore
actual fun webPToJpeg(webP: WebP): Jpeg = TODO()

@ProbablyCanOptimizeWayMore
actual fun webPToPng(webP: WebP): Png = TODO()
