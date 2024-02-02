package matt.image

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import matt.lang.anno.ProbablyCanOptimizeWayMore
import matt.lang.anno.SupportedByChatGPT
import java.io.ByteArrayOutputStream


@ProbablyCanOptimizeWayMore
actual fun pngToJpeg(png: Png): Jpeg {
    val bitmap = png.toBitmap()
    val out = ByteArrayOutputStream()
    bitmap.compress(JPEG, 100, out)
    return Jpeg(out.toByteArray())
}

@ProbablyCanOptimizeWayMore
actual fun jpegToPng(jpeg: Jpeg): Png {
    val bitmap = jpeg.toBitmap()
    val out = ByteArrayOutputStream()
    bitmap.compress(PNG, 100, out)
    return Png(out.toByteArray())
}

@ProbablyCanOptimizeWayMore
actual fun argbToPng(argb: Argb): Png {
    val bitmap = argb.toBitmap()
    val out = ByteArrayOutputStream()
    bitmap.compress(PNG, 100, out)
    return Png(out.toByteArray())
}

@ProbablyCanOptimizeWayMore
actual fun argbToJpeg(argb: Argb): Jpeg = TODO()


fun NativeAndroidAndSkiaDecodableRaster.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this.bytes, 0, bytes.size)

@SupportedByChatGPT
fun Argb.toBitmap(): Bitmap {

    val bitmap = Bitmap.createBitmap(
        this.width,
        derivedHeight,
        Bitmap.Config.ARGB_8888
    )

    bitmap.setPixels(
        this.pixels,
        0,
        width,
        0,
        0,
        width,
        derivedHeight
    )


    return bitmap
}
