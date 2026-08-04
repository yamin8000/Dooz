/*
 *     Dooz
 *     PlayerSettings.kt Created/Updated by Yamin Siahmargooei at 2022/9/26
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

package io.github.yamin8000.dooz.feature_settings.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.components.ClickableShapes
import io.github.yamin8000.dooz.common.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.common.ui.components.shapes
import io.github.yamin8000.dooz.common.ui.components.toName
import io.github.yamin8000.dooz.common.ui.components.toShape
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import io.github.yamin8000.dooz.common.util.Constants

@Composable
internal fun PlayerCustomization(
    firstPlayerName: String,
    onFirstPlayerNameChange: (String) -> Unit,
    secondPlayerName: String,
    onSecondPlayerNameChange: (String) -> Unit,
    firstPlayerShape: String,
    onFirstPlayerShapeChange: (String) -> Unit,
    secondPlayerShape: String,
    onSecondPlayerShapeChange: (String) -> Unit,
) {
    SettingsItemCard(
        title = stringResource(R.string.player_names),
        content = {
            PlayerNamesCustomizer(
                firstPlayerName = firstPlayerName,
                onFirstPlayerNameChange = onFirstPlayerNameChange,
                secondPlayerName = secondPlayerName,
                onSecondPlayerNameChange = onSecondPlayerNameChange
            )
            AppText(
                text = stringResource(R.string.player_shapes),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = Sizes.Large),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            PlayerShapesCustomizer(
                firstPlayerShape = firstPlayerShape,
                onFirstPlayerShapeChange = onFirstPlayerShapeChange,
                secondPlayerShape = secondPlayerShape,
                onSecondPlayerShapeChange = onSecondPlayerShapeChange
            )
        }
    )
}

@Composable
internal fun PlayerShapesCustomizer(
    firstPlayerShape: String,
    onFirstPlayerShapeChange: (String) -> Unit,
    secondPlayerShape: String,
    onSecondPlayerShapeChange: (String) -> Unit
) {
    ClickableShapes(
        shapes = shapes,
        lastSelectedShape = firstPlayerShape.toShape(),
        header = { SingleLinePersianText(stringResource(R.string.first_player_shape)) },
        onShapeSelected = { onFirstPlayerShapeChange(it.toName() ?: Constants.Shapes.RING_SHAPE) }
    )
    ClickableShapes(
        shapes = shapes,
        lastSelectedShape = secondPlayerShape.toShape(),
        header = { SingleLinePersianText(stringResource(R.string.second_player_shape)) },
        onShapeSelected = { onSecondPlayerShapeChange(it.toName() ?: Constants.Shapes.X_SHAPE) }
    )
}

@Composable
private fun PlayerNamesCustomizer(
    firstPlayerName: String,
    onFirstPlayerNameChange: (String) -> Unit,
    secondPlayerName: String,
    onSecondPlayerNameChange: (String) -> Unit
) {
    NameField(
        label = stringResource(R.string.second_player_name),
        placeholder = stringResource(R.string.enter_name),
        value = secondPlayerName,
        onValueChange = onSecondPlayerNameChange
    )
    NameField(
        label = stringResource(R.string.first_player_name),
        placeholder = stringResource(R.string.enter_name),
        value = firstPlayerName,
        onValueChange = onFirstPlayerNameChange
    )
}