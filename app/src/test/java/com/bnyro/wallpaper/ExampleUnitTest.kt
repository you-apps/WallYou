package com.bnyro.wallpaper

import com.bnyro.wallpaper.util.BitmapProcessor.multiply
import com.bnyro.wallpaper.util.TimeHelper
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testTimeRange() {
        assert(TimeHelper.isInTimeRange(500, 200, 700))
        assert(!TimeHelper.isInTimeRange(500, 600, 700))
        assert(TimeHelper.isInTimeRange(500, 700, 600))
    }

    @Test
    fun test() {
        val matrix1 = floatArrayOf(
            1f, 2f, 3f, 4f
        )

        val matrix2 = floatArrayOf(
            5f, 6f, 7f, 8f
        )

        val expected = floatArrayOf(
            19f, 22f, 43f, 50f
        )

        assert(multiply(matrix1, matrix2).contentEquals(expected))
    }
}
