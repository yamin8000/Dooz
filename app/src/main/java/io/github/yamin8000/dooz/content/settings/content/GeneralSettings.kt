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

package io.github.yamin8000.dooz.content.settings.content

import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings.ThemeSetting
import io.github.yamin8000.dooz.ui.composables.InfoCard
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.RadioGroup
import io.github.yamin8000.dooz.ui.composables.SwitchWithText

@Composable
internal fun ThemeChangerCard(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    val resources = LocalContext.current.resources
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = stringResource(R.string.theme),
        content = {
            RadioGroup(
                options = ThemeSetting.values().toList(),
                currentOption = currentTheme,
                onOptionChange = onCurrentThemeChange,
                optionStringProvider = { resources.getString(it.persianNameStringResource) }
            )
        },
        footer = {
            if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                DynamicThemeNotice()
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
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = stringResource(R.string.effects),
        content = {
            SwitchWithText(
                caption = stringResource(R.string.sound_effects),
                checked = isSoundOn,
                onCheckedChange = isSoundOnChange
            )
            SwitchWithText(
                caption = stringResource(R.string.haptic_feedback),
                checked = isVibrationOn,
                onCheckedChange = isVibrationOnChange
            )
        }
    )
}

@Composable
fun DynamicThemeNotice() {
    PersianText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}
