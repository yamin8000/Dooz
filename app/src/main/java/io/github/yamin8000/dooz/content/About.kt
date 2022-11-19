/*
 *     Dooz/Dooz.app.main
 *     About.kt Copyrighted by Yamin Siahmargooei at 2022/11/13
 *     About.kt Last modified at 2022/11/13
 *     This file is part of Dooz/Dooz.app.main.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz/Dooz.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz/Dooz.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.BuildConfig
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.Ripple

@Preview
@Composable
fun AboutContent() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val uriHandler = LocalUriHandler.current
            val sourceUri = stringResource(R.string.github_source)
            val licenseUri = stringResource(R.string.license_link)
            Ripple(
                onClick = { uriHandler.openUri(licenseUri) },
                content = {
                    Image(
                        painterResource(id = R.drawable.ic_gplv3),
                        stringResource(id = R.string.gplv3_image_description),
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                PersianText(
                    text = stringResource(R.string.version_name),
                )
                PersianText(
                    text = BuildConfig.VERSION_NAME
                )
            }
            PersianText(
                stringResource(id = R.string.license_header),
                modifier = Modifier.fillMaxWidth()
            )
            Ripple(
                onClick = { uriHandler.openUri(sourceUri) },
                content = {
                    Text(
                        text = sourceUri,
                        textDecoration = TextDecoration.Underline
                    )
                }
            )
        }
    }
}