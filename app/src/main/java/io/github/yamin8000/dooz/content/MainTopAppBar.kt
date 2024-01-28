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

package io.github.yamin8000.dooz.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Help
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.composables.AnimatedAppIcon
import io.github.yamin8000.dooz.ui.composables.ClickableIcon
import io.github.yamin8000.dooz.ui.composables.PersianText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClick: () -> Unit,
    onAboutIconClick: () -> Unit
) {
    val appName = stringResource(R.string.app_name)
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = { AnimatedAppIcon() },
        actions = {
            SettingsIcon(onSettingsIconClick)
            AboutIcon(onAboutIconClick)
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

@Composable
fun AboutIcon(
    onAboutIconClick: () -> Unit
) {
    ClickableIcon(
        imageVector = Icons.AutoMirrored.TwoTone.Help,
        contentDescription = stringResource(R.string.about),
        onClick = onAboutIconClick
    )
}

@Composable
private fun SettingsIcon(
    onSettingsIconClick: () -> Unit
) {
    ClickableIcon(
        imageVector = Icons.TwoTone.Settings,
        contentDescription = stringResource(R.string.settings),
        onClick = onSettingsIconClick
    )
}

@Composable
private fun NavigationIcon(
    appName: String
) {
    Icon(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = appName,
        tint = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground)
    )
}