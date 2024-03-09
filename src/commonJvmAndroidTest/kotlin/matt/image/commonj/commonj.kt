package matt.image.commonj

import matt.image.common.Argb
import matt.image.common.Svg
import kotlin.test.Test


class CommonJvmAndroidImageTests {
    @Test
    fun doImageStuff() {
        Svg("svg")
        Argb(IntArray(3) { 1 }, 3)
    }
}
