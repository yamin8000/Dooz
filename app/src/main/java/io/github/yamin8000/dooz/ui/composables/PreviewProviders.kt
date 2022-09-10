/*
 *     Dooz
 *     PreviewProviders.kt Created/Updated by Yamin Siahmargooei at 2022/9/10
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

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.util.Constants

class TextProvider : PreviewParameterProvider<String> {
    override val values = listOf("سلام", "یمین").asSequence()
}

class BooleanProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = listOf(true, false).asSequence()
}

class PlayerProvider : PreviewParameterProvider<Player> {
    override val values = listOf(
        Player("یمین", Constants.Shapes.xShape)
    ).asSequence()
}