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

package io.github.yamin8000.dooz.content.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.ClickableShapes
import io.github.yamin8000.dooz.ui.composables.InfoCard
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.SingleLinePersianText
import io.github.yamin8000.dooz.ui.shapes
import io.github.yamin8000.dooz.ui.toName
import io.github.yamin8000.dooz.ui.toShape
import io.github.yamin8000.dooz.util.Constants

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
    onSave: () -> Unit
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.player_names),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        content = {
            PlayerNamesCustomizer(
                firstPlayerName = firstPlayerName,
                onFirstPlayerNameChange = onFirstPlayerNameChange,
                secondPlayerName = secondPlayerName,
                onSecondPlayerNameChange = onSecondPlayerNameChange
            )
            PersianText(
                text = stringResource(R.string.player_shapes),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp),
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
            Button(
                onClick = { onSave() },
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .padding(top = 16.dp),
                content = {
                    PersianText(
                        text = stringResource(R.string.save),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
        onShapeSelected = { onFirstPlayerShapeChange(it.toName() ?: Constants.Shapes.ringShape) }
    )
    ClickableShapes(
        shapes = shapes,
        lastSelectedShape = secondPlayerShape.toShape(),
        header = { SingleLinePersianText(stringResource(R.string.second_player_shape)) },
        onShapeSelected = { onSecondPlayerShapeChange(it.toName() ?: Constants.Shapes.xShape) }
    )
}

@Composable
internal fun PlayerNamesCustomizer(
    firstPlayerName: String,
    onFirstPlayerNameChange: (String) -> Unit,
    secondPlayerName: String,
    onSecondPlayerNameChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NameField(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.second_player_name),
            placeholder = stringResource(R.string.enter_name),
            value = secondPlayerName,
            onValueChange = onSecondPlayerNameChange
        )
        NameField(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.first_player_name),
            placeholder = stringResource(R.string.enter_name),
            value = firstPlayerName,
            onValueChange = onFirstPlayerNameChange
        )
    }
}

@Composable
internal fun NameField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { PersianText(text = label, fontSize = 12.sp) },
        placeholder = { PersianText(text = placeholder, fontSize = 12.sp) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}