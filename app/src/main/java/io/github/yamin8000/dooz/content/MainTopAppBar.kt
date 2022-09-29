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

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.composables.ClickableIcon
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClick: () -> Unit
) {
    val appName = stringResource(id = R.string.app_name)
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { PersianText(text = appName, fontSize = 20.sp) },
        navigationIcon = { NavigationIcon(appName) },
        actions = {
            SettingsIcon(onSettingsIconClick)
        }
    )
}

@Composable
private fun SettingsIcon(
    onSettingsIconClick: () -> Unit
) {
    ClickableIcon(
        iconPainter = painterResource(R.drawable.ic_settings),
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
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground),
        tint = MaterialTheme.colorScheme.background
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { MainTopAppBar(TopAppBarDefaults.enterAlwaysScrollBehavior()) {} }
}