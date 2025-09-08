/*
 *     Dooz
 *     MainTopAppBar.kt Created/Updated by Yamin Siahmargooei at 2022/9/21
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Help
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.components.AnimatedAppIcon
import io.github.yamin8000.dooz.ui.components.ClickableIcon
import io.github.yamin8000.dooz.ui.components.PersianText
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        MainTopAppBar(
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onSettingsIconClick = {},
            onAboutIconClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClick: () -> Unit,
    onAboutIconClick: () -> Unit
) {
    val appName = stringResource(R.string.app_name)
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = { AnimatedAppIcon() },
        actions = {
            ClickableIcon(
                imageVector = Icons.TwoTone.Settings,
                contentDescription = stringResource(R.string.settings),
                onClick = onSettingsIconClick
            )

            ClickableIcon(
                imageVector = Icons.AutoMirrored.TwoTone.Help,
                contentDescription = stringResource(R.string.about),
                onClick = onAboutIconClick
            )
        },
        title = {
            PersianText(
                text = appName,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}