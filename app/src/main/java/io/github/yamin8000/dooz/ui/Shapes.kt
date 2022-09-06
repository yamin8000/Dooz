/*
 *     Dooz
 *     Shapes.kt Created/Updated by Yamin Siahmargooei at 2022/9/7
 *     This file is part of Dooz.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
}

val XShape = GenericShape { size, _ ->
    moveTo(0f, size.height / 5f)
    lineTo(size.width / 5f, 0f)
    lineTo(size.width, size.height * 0.8f)
    lineTo(size.width * 0.8f, size.height)
    moveTo(size.width * 0.8f, 0f)
    lineTo(size.width, size.height / 5f)
    lineTo(size.width / 5f, size.height)
    lineTo(0f, size.height * 0.8f)
}

val RingShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val thickness = size.height / 4
        val outerCircle = Path().apply {
            addOval(Rect(0f, 0f, size.width, size.height))
        }
        val innerCircle = Path().apply {
            addOval(
                Rect(
                    thickness,
                    thickness,
                    size.width - thickness,
                    size.height - thickness
                )
            )
        }
        val ring = Path()
        ring.op(outerCircle, innerCircle, PathOperation.Difference)
        return Outline.Generic(ring)
    }

}

@Preview(showBackground = true)
@Composable
private fun ShapePreview() {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RingShape)
            .background(MaterialTheme.colorScheme.secondary)
    )
}