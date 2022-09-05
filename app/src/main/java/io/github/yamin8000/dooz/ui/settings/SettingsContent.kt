/*
 *     Dooz
 *     SettingsContent.kt Created/Updated by Yamin Siahmargooei at 2022/9/6
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

package io.github.yamin8000.dooz.ui.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.DoozTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Preview(showBackground = true)
@Composable
fun SettingsContent(
    navController: NavController? = null
) {
    //val playersType = getPlayersType()

    DoozTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                PersianText(
                    text = "تنظیمات",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                //GamePlayersTypeSwitch(gamePlayersType = )
            }
        }
    }
}

//private suspend fun getPlayersType(
//    context: Context
//): Flow<String> {
//    val type = context.settings.data.map {
//        it[stringPreferencesKey("")]
//    }
//}

@Composable
private fun GamePlayersTypeSwitch(
    gamePlayersType: GamePlayersType
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text =
            if (gamePlayersType == GamePlayersType.PvP) stringResource(R.string.play_with_human)
            else stringResource(R.string.play_with_computer)
        )
        Switch(
            checked = gamePlayersType == GamePlayersType.PvP,
            onCheckedChange = { isChecked ->
                //if (isChecked) gamePlayersType = GamePlayersType.PvP
                //else gamePlayersType = GamePlayersType.PvC
            }
        )
    }
}
