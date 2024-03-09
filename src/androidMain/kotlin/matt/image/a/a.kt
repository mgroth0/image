package matt.image.a

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import matt.image.a.LossMode.Lossless
import matt.image.a.LossMode.Lossy
import matt.image.common.Argb
import matt.image.common.Jpeg
import matt.image.common.NativeAndroidAndSkiaDecodableRaster
import matt.image.common.Png
import matt.image.common.WebP
import matt.lang.anno.ProbablyCanOptimizeWayMore
import matt.lang.anno.SupportedByChatGPT
import java.io.ByteArrayOutputStream


fun NativeAndroidAndSkiaDecodableRaster.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

@SupportedByChatGPT
fun Argb.toBitmap(): Bitmap {

    val bitmap =
        Bitmap.createBitmap(
            width,
            derivedHeight,
            Bitmap.Config.ARGB_8888
        )

    bitmap.setPixels(
        pixels,
        0,
        width,
        0,
        0,
        width,
        derivedHeight
    )


    return bitmap
}

private const val BITMAP_COMPRESS_MAX_QUALITY = 100

@ProbablyCanOptimizeWayMore
fun Bitmap.toPng(): Png {
    val out = ByteArrayOutputStream()
    compress(PNG, BITMAP_COMPRESS_MAX_QUALITY, out)
    return Png(out.toByteArray())
}

@ProbablyCanOptimizeWayMore
fun Bitmap.toJpeg(): Jpeg {
    val out = ByteArrayOutputStream()
    compress(JPEG, BITMAP_COMPRESS_MAX_QUALITY, out)
    return Jpeg(out.toByteArray())
}

enum class LossMode {
    Lossy, Lossless
}

@ProbablyCanOptimizeWayMore
fun Bitmap.toWebP(lossMode: LossMode): WebP {
    val out = ByteArrayOutputStream()
    compress(
        when (lossMode) {
            Lossy    -> CompressFormat.WEBP_LOSSY
            Lossless -> CompressFormat.WEBP_LOSSLESS
        },
        BITMAP_COMPRESS_MAX_QUALITY, out
    )
    return WebP(out.toByteArray())
}

