package matt.image.test


import matt.image.icon.ICON_SIZES
import matt.test.assertions.JupiterTestAssertions.assertRunsInOneMinute
import kotlin.test.Test

class ImageTests {
    @Test
    fun initValues() = assertRunsInOneMinute {
        ICON_SIZES
    }
}