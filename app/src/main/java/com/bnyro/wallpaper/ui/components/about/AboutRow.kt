package com.bnyro.wallpaper.ui.components.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutRow(
    title: String,
    summary: String? = null,
    imageVector: ImageVector? = null,
    painterResource: Painter? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(0.dp, 3.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick.invoke()
            }
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageVector != null) {
            Icon(imageVector, null)
        } else if (painterResource != null) {
            Icon(painterResource, null)
        }
        Spacer(
            modifier = Modifier.width(15.dp)
        )
        Column {
            Text(
                text = title,
                fontSize = 18.sp
            )
            if (summary != null) {
                Text(
                    text = summary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
