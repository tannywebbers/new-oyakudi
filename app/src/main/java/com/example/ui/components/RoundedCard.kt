package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SapaBorder
import com.example.ui.theme.SapaSurface

@Composable
fun RoundedCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 0.dp,
    border: BorderStroke? = BorderStroke(1.dp, SapaBorder),
    backgroundColor: Color = SapaSurface,
    contentPadding: Dp = 20.dp,
    shape: Shape = RoundedCornerShape(cornerRadius),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(elevation, shape, ambientColor = Color(0x10000000), spotColor = Color(0x10000000))
                } else Modifier
            ),
        shape = shape,
        color = backgroundColor,
        border = border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            content = content
        )
    }
}
