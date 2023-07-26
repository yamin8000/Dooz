/*
 *     Dooz/Dooz.app.main
 *     AdMainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/4/22
 *     AdMainActivity.kt Last modified at 2023/4/22
 *     This file is part of Dooz/Dooz.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
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

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import io.github.yamin8000.dooz.ad.AdConstants
import io.github.yamin8000.dooz.ad.AdHelper
import io.github.yamin8000.dooz.ad.TapsellAdContent
import io.github.yamin8000.dooz.util.Utility.log
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        initTapsellAd()
        setContent {
            MainNavigation(adContent = { AdContent() })
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }


    @Composable
    fun AdContent() {
        var adView by remember { mutableStateOf<ViewGroup?>(null) }
        var adId: String by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            adId = AdHelper.requestTapsellAd(this@MainActivity)
            AdHelper.showTapsellAd(this@MainActivity, adId, adView)
        }

        TapsellAdContent(
            modifier = Modifier
                .wrapContentHeight()
                .padding(4.dp)
                .fillMaxWidth(),
            onCreated = { adView = it },
            onUpdate = { adView = it }
        )
    }

    private fun initTapsellAd() {
        log("ad init")
        TapsellPlus.initialize(this, AdConstants.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(ads: AdNetworks?) {
                log(ads?.name ?: "Unknown ad name")
            }

            override fun onInitializeFailed(ads: AdNetworks?, error: AdNetworkError?) {
                log(error?.errorMessage ?: "Unknown tapsell init error")
            }
        })
    }
}
