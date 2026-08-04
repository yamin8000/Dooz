package io.github.yamin8000.dooz.feature_settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.dooz.common.domain.repository.SettingsRepository
import io.github.yamin8000.dooz.common.util.Constants
import io.github.yamin8000.dooz.common.util.Utility.log
import io.github.yamin8000.dooz.feature_settings.ui.SettingsEvent.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log(throwable.stackTraceToString())
    }

    private val scope = CoroutineScope(
        SupervisorJob() + viewModelScope.coroutineContext + exceptionHandler
    )

    private var _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart { reloadSettings() }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SettingsState()
        )

    private var eventChannel = Channel<SettingsEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    private suspend fun reloadSettings() {
        _state.update {
            it.copy(
                gameSize = settings.getGameSize(),
                gamePlayersType = settings.getGamePlayersType(),
                firstPlayerName = settings.getFirstPlayerName(),
                secondPlayerName = settings.getSecondPlayerName(),
                firstPlayerShape = settings.getFirstPlayerShape(),
                secondPlayerShape = settings.getSecondPlayerShape(),
                aiDifficulty = settings.getAiDifficulty(),
                theme = settings.getTheme(),
                isSoundOn = settings.getSoundState(),
                isVibrationOn = settings.getVibrationState(),
                firstPlayerPolicy = settings.getFirstPlayerPolicy()
            )
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnUpdateAiDifficulty -> {
                _state.update { it.copy(aiDifficulty = action.difficulty) }
                scope.launch { settings.setAiDifficulty(action.difficulty) }
            }

            is SettingsAction.OnUpdateFirstPlayerName -> {
                _state.update { it.copy(firstPlayerName = action.name) }
                scope.launch { settings.setFirstPlayerName(action.name) }
            }

            is SettingsAction.OnUpdateFirstPlayerPolicy -> {
                _state.update { it.copy(firstPlayerPolicy = action.policy) }
                scope.launch { settings.setFirstPlayerPolicy(action.policy) }
            }

            is SettingsAction.OnUpdateFirstPlayerShape -> {
                if (isPairValid(action.shape, state.value.secondPlayerShape)) {
                    _state.update { it.copy(firstPlayerShape = action.shape) }
                    scope.launch { settings.setFirstPlayerShape(action.shape) }
                }
            }

            is SettingsAction.OnUpdateGamePlayerType -> {
                _state.update { it.copy(gamePlayersType = action.type) }
                scope.launch { settings.setGamePlayerType(action.type) }
            }

            is SettingsAction.OnUpdateGameSize -> {
                if (action.size in Constants.gameSizeRange) {
                    _state.update { it.copy(gameSize = action.size) }
                    scope.launch { settings.setGameSize(action.size) }
                }
            }

            is SettingsAction.OnUpdateSecondPlayerName -> {
                _state.update { it.copy(secondPlayerName = action.name) }
                scope.launch { settings.setSecondPlayerName(action.name) }
            }

            is SettingsAction.OnUpdateSecondPlayerShape -> {
                if (isPairValid(action.shape, state.value.firstPlayerShape)) {
                    _state.update { it.copy(secondPlayerShape = action.shape) }
                    scope.launch { settings.setSecondPlayerShape(action.shape) }
                }
            }

            is SettingsAction.OnUpdateSoundState -> {
                _state.update { it.copy(isSoundOn = action.state) }
                scope.launch { settings.setSoundState(action.state) }
            }

            is SettingsAction.OnUpdateTheme -> {
                _state.update { it.copy(theme = action.theme) }
                scope.launch {
                    settings.setTheme(action.theme)
                    eventChannel.send(OnThemeChanged(action.theme))
                }
            }

            is SettingsAction.OnUpdateVibrationState -> {
                _state.update { it.copy(isVibrationOn = action.state) }
                scope.launch { settings.setVibrationState(action.state) }
            }

            is SettingsAction.OnTabChanged -> {
                _state.update { it.copy(currentTab = action.tab) }
            }
        }
    }

    private fun isPairValid(
        first: String,
        second: String
    ) = if (first.isEmpty() || second.isEmpty()) false else first.trim() != second.trim()
}