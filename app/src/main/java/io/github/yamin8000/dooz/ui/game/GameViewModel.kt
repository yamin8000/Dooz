package io.github.yamin8000.dooz.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    private val scope = CoroutineScope(
        SupervisorJob() + viewModelScope.coroutineContext + exceptionHandler
    )
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    private var _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    init {

    }

    fun onAction(action: GameAction) {
        when (action) {

            else -> {}
        }
    }
}