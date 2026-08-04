package io.github.yamin8000.dooz.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.common.domain.repository.SettingsRepository
import io.github.yamin8000.dooz.common.ui.theme.AppTheme
import io.github.yamin8000.dooz.feature_about.ui.AboutScreen
import io.github.yamin8000.dooz.feature_game.ui.GameScreen
import io.github.yamin8000.dooz.feature_settings.ui.SettingsScreen
import io.github.yamin8000.dooz.ui.navigation.Nav
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settings: SettingsRepository

    private var theme by mutableStateOf(ThemeSetting.System)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        runBlocking {
            theme = settings.getTheme()
        }

        setContent {
            AppTheme(
                isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
                isDynamicColor = theme == ThemeSetting.System,
                content = {
                    Column {
                        val navController = rememberNavController()
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = Nav.Route.Game(),
                            builder = {
                                composable(Nav.Route.Game()) {
                                    GameScreen(
                                        onNavigateToSettings = { navController.navigate(Nav.Route.Settings()) },
                                        onNavigateToAbout = { navController.navigate(Nav.Route.About()) }
                                    )
                                }

                                composable(Nav.Route.Settings()) {
                                    SettingsScreen(
                                        onThemeChanged = { theme = it },
                                        onBackClick = { navController.navigateUp() }
                                    )
                                }

                                composable(Nav.Route.About()) {
                                    AboutScreen(onBackClick = { navController.navigateUp() })
                                }
                            }
                        )
                    }
                }
            )
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun isDarkTheme(
        themeSetting: ThemeSetting,
        isSystemInDarkTheme: Boolean
    ) = when (themeSetting) {
        ThemeSetting.Light -> false
        ThemeSetting.System -> isSystemInDarkTheme
        else -> themeSetting == ThemeSetting.Dark
    }
}