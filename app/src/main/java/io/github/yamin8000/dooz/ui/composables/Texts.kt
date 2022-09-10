/*
 *     Dooz
 *     Texts.kt Created/Updated by Yamin Siahmargooei at 2022/9/6
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

package io.github.yamin8000.dooz.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.ui.theme.Samim

@Preview(showBackground = true)
@Composable
fun PersianText(
    @PreviewParameter(TextProvider::class)
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    fontFamily: FontFamily = Samim,
    textAlign: TextAlign = TextAlign.Right,
    color: Color = Color.Unspecified
) {
    Text(
        text,
        modifier = modifier,
        fontFamily = fontFamily,
        textAlign = textAlign,
        fontSize = fontSize,
        color = color
    )
}