/*
 *     Dooz
 *     GeneralSettings.kt Created/Updated by Yamin Siahmargooei at 2022/9/26
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

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.common.ui.components.SwitchWithText
import io.github.yamin8000.dooz.common.ui.theme.Sizes

@Composable
internal fun ThemeChangerCard(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }

    if (isShowingThemeDialog) {
        ThemeChangerDialog(
            currentTheme = currentTheme,
            onCurrentThemeChange = onCurrentThemeChange,
            onDismiss = { isShowingThemeDialog = false }
        )
    }

    SettingsItemCard(
        title = stringResource(R.string.theme),
        content = {
            SettingsItem(
                onClick = { isShowingThemeDialog = true },
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.DisplaySettings,
                        contentDescription = stringResource(R.string.theme)
                    )
                    SingleLinePersianText(stringResource(currentTheme.persianNameStringResource))
                }
            )
            if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                DynamicThemeNotice()
            }
        }
    )
}

@Composable
private fun ThemeChangerDialog(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.entries.toTypedArray() }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*ignored*/ },
        title = { AppText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(Sizes.Large)
                    .selectableGroup()
                    .fillMaxWidth(),
                content = {
                    themes.forEach { theme ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = theme == currentTheme,
                                    role = Role.RadioButton,
                                    onClick = {
                                        onCurrentThemeChange(theme)
                                        onDismiss()
                                    }
                                ),
                            content = {
                                RadioButton(
                                    selected = theme == currentTheme,
                                    onClick = null,
                                    modifier = Modifier.padding(start = Sizes.Medium)
                                )
                                AppText(
                                    text = stringResource(theme.persianNameStringResource),
                                    modifier = Modifier.padding(vertical = Sizes.Large)
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}

@Composable
internal fun EffectsCard(
    isSoundOn: Boolean,
    isSoundOnChange: (Boolean) -> Unit,
    isVibrationOn: Boolean,
    isVibrationOnChange: (Boolean) -> Unit
) {
    SettingsItemCard(
        title = stringResource(R.string.effects),
        content = {
            SwitchWithText(
                onCheckedChange = isSoundOnChange,
                checked = isSoundOn,
                caption = stringResource(R.string.sound_effects)
            )
            SwitchWithText(
                onCheckedChange = isVibrationOnChange,
                checked = isVibrationOn,
                caption = stringResource(R.string.haptic_feedback)
            )
        }
    )
}

@Composable
private fun DynamicThemeNotice() {
    AppText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}
