package matt.image

import matt.image.common.Argb
import matt.image.common.Jpeg
import matt.image.common.Png
import matt.image.common.WebP


internal expect fun pngToJpeg(png: Png): Jpeg


internal expect fun jpegToPng(jpeg: Jpeg): Png


internal expect fun argbToJpeg(argb: Argb): Jpeg
internal expect fun argbToPng(argb: Argb): Png

internal expect fun webPToJpeg(webP: WebP): Jpeg
internal expect fun webPToPng(webP: WebP): Png
