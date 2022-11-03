package com.bnyro.wallpaper.ui.components

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.wallpaper.util.Preferences

@Composable
fun ImageFilterSlider(
    prefKey: String,
    title: String,
    defValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    changeDelay: Long = 300L,
    onValueChange: (Float) -> Unit = {}
) {
    val handler = Handler(Looper.getMainLooper())

    var value by remember {
        mutableStateOf(
            Preferences.getFloat(prefKey, defValue)
        )
    }

    var lastChanged = System.currentTimeMillis()

    val delayedRunnable = Runnable {
        if (System.currentTimeMillis() - lastChanged < changeDelay) return@Runnable
        lastChanged = System.currentTimeMillis()
        onValueChange.invoke(value)
    }

    Column {
        Text(title)
        Spacer(Modifier.height(3.dp))
        SliderWithLabel(
            value = value,
            valueRange = valueRange,
            onValueChange = {
                value = it
                Preferences.edit { putFloat(prefKey, it) }
                handler.postDelayed(delayedRunnable, changeDelay)
            }
        )
    }
}
