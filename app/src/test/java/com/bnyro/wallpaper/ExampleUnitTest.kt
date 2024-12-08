package com.bnyro.wallpaper

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
}
