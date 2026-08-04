package io.github.yamin8000.dooz.feature_settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.components.ScaffoldWithTitle
import io.github.yamin8000.dooz.common.ui.theme.AppTheme
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import io.github.yamin8000.dooz.common.util.Utility.ObserverEvent
import io.github.yamin8000.dooz.feature_settings.domain.model.SettingsTab
import io.github.yamin8000.dooz.feature_settings.ui.UiSettingsTab.Companion.toUi
import io.github.yamin8000.dooz.feature_settings.ui.components.AiDifficultyCard
import io.github.yamin8000.dooz.feature_settings.ui.components.EffectsCard
import io.github.yamin8000.dooz.feature_settings.ui.components.GameSizeChanger
import io.github.yamin8000.dooz.feature_settings.ui.components.GeneralGameSettings
import io.github.yamin8000.dooz.feature_settings.ui.components.PlayerCustomization
import io.github.yamin8000.dooz.feature_settings.ui.components.ThemeChangerCard

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        SettingsContent(
            state = SettingsState(),
            onAction = {},
            onBackClick = {},
        )
    }
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onThemeChanged: (ThemeSetting) -> Unit,
    modifier: Modifier = Modifier,
    vm: SettingsViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    ObserverEvent(vm.eventFlow) { event ->
        when (event) {
            is SettingsEvent.OnThemeChanged -> onThemeChanged(event.theme)
        }
    }

    SettingsContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick,
        onAction = { vm.onAction(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    onBackClick: () -> Unit,
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ScaffoldWithTitle(
        modifier = modifier,
        title = stringResource(R.string.settings),
        onBackClick = onBackClick,
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Sizes.Large),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        content = {
                            SecondaryScrollableTabRow(
                                selectedTabIndex = state.currentTab.ordinal,
                                tabs = {
                                    SettingsTab.entries.forEach { tab ->
                                        Tab(
                                            selected = tab == state.currentTab,
                                            onClick = { onAction(SettingsAction.OnTabChanged(tab)) },
                                            text = { AppText(tab.toUi().name) },
                                            icon = {
                                                Icon(
                                                    imageVector = tab.toUi().icon,
                                                    contentDescription = tab.toUi().name
                                                )
                                            }
                                        )
                                    }
                                }
                            )
                            when (state.currentTab) {
                                SettingsTab.General -> {
                                    ThemeChangerCard(
                                        currentTheme = state.theme,
                                        onCurrentThemeChange = { newTheme ->
                                            onAction(SettingsAction.OnUpdateTheme(newTheme))
                                        }
                                    )
                                    EffectsCard(
                                        isSoundOn = state.isSoundOn,
                                        isSoundOnChange = {
                                            onAction(SettingsAction.OnUpdateSoundState(it))
                                        },
                                        isVibrationOn = state.isVibrationOn,
                                        isVibrationOnChange = {
                                            onAction(SettingsAction.OnUpdateVibrationState(it))
                                        }
                                    )
                                }

                                SettingsTab.Game -> {
                                    GeneralGameSettings(
                                        gamePlayersType = state.gamePlayersType,
                                        onPlayerTypeChange = {
                                            onAction(SettingsAction.OnUpdateGamePlayerType(it))
                                        },
                                        firstPlayerPolicy = state.firstPlayerPolicy,
                                        onFirstPlayerPolicyChange = {
                                            onAction(SettingsAction.OnUpdateFirstPlayerPolicy(it))
                                        }
                                    )
                                    AiDifficultyCard(
                                        aiDifficulty = state.aiDifficulty,
                                        onDifficultyChanged = {
                                            onAction(SettingsAction.OnUpdateAiDifficulty(it))
                                        }
                                    )
                                    GameSizeChanger(
                                        gameSize = state.gameSize,
                                        onGameSizeIncrease = {
                                            onAction(SettingsAction.OnUpdateGameSize(state.gameSize + 1))
                                        },
                                        onGameSizeDecrease = {
                                            onAction(SettingsAction.OnUpdateGameSize(state.gameSize - 1))
                                        }
                                    )
                                }

                                SettingsTab.Players -> {
                                    PlayerCustomization(
                                        firstPlayerName = state.firstPlayerName,
                                        onFirstPlayerNameChange = {
                                            onAction(SettingsAction.OnUpdateFirstPlayerName(it))
                                        },
                                        secondPlayerName = state.secondPlayerName,
                                        onSecondPlayerNameChange = {
                                            onAction(SettingsAction.OnUpdateSecondPlayerName(it))
                                        },
                                        firstPlayerShape = state.firstPlayerShape,
                                        onFirstPlayerShapeChange = {
                                            onAction(SettingsAction.OnUpdateFirstPlayerShape(it))
                                        },
                                        secondPlayerShape = state.secondPlayerShape,
                                        onSecondPlayerShapeChange = {
                                            onAction(SettingsAction.OnUpdateSecondPlayerShape(it))
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}