package io.github.yamin8000.dooz.feature_game.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.dooz.common.domain.model.DoozCell
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.GameType
import io.github.yamin8000.dooz.common.domain.model.Player
import io.github.yamin8000.dooz.common.domain.model.PlayerType
import io.github.yamin8000.dooz.common.domain.repository.SettingsRepository
import io.github.yamin8000.dooz.common.ui.components.RingShape
import io.github.yamin8000.dooz.common.ui.components.XShape
import io.github.yamin8000.dooz.common.ui.components.toName
import io.github.yamin8000.dooz.common.ui.components.toShape
import io.github.yamin8000.dooz.common.util.Constants
import io.github.yamin8000.dooz.common.util.Constants.aiPlayDelayRange
import io.github.yamin8000.dooz.feature_game.domain.logic.GameLogic
import io.github.yamin8000.dooz.feature_game.domain.logic.SimpleGameLogic
import io.github.yamin8000.dooz.feature_game.util.Utility.to3D
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("==>", throwable.stackTraceToString())
    }

    private val scope = CoroutineScope(
        SupervisorJob() + viewModelScope.coroutineContext + exceptionHandler
    )

    private var _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var gameLogic: GameLogic? = null

    private var isSoundOn = true
    private var isVibrationOn = true

    private var eventChannel = Channel<GameEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        scope.launch { prepareGame() }
    }

    fun onAction(action: GameAction) {
        when (action) {
            GameAction.NewGame -> {
                scope.launch {
                    prepareGame()

                    _state.update { it.copy(isGameStarted = true) }

                    if (state.value.firstPlayerPolicy == FirstPlayerPolicy.DiceRolling) {
                        dummyDiceRolling()
                    }

                    if (isAiTurnToPlay()) {
                        scope.launch { playCellByAi() }
                    }
                }
            }

            is GameAction.PlayCell -> {
                checkIfGameIsFinished()
                changeCellOwner(action.cell)
                checkIfGameIsFinished()

                if (isAiTurnToPlay()) {
                    scope.launch { asyncPlayCellByAi() }
                }
            }

            GameAction.Undo -> {
                if (state.value.gamePlayersType == GamePlayersType.PvC) {
                    //2 undo action
                    undo()
                    undo()
                } else {
                    //1 undo action
                    undo()
                }
            }
        }
    }

    private suspend fun asyncPlayCellByAi() {
        delay(Random.nextLong(aiPlayDelayRange).milliseconds)
        playCellByAi()
    }

    fun undo() {
        if (state.value.lastPlayedCells.isNotEmpty()) {
            val last = state.value.lastPlayedCells.last()
            last.owner = null
            val lastIndex = state.value.gameCells.indexOf(last)
            state.value.gameCells[lastIndex] = last

            state.value.lastPlayedCells.removeAt(state.value.lastPlayedCells.size - 1)

            prepareGameLogic()
            _state.update {
                it.copy(
                    winner = null,
                    isGameFinished = false,
                    isGameDraw = false,
                    winnerCells = mutableStateListOf()
                )
            }

            if (isAiTurnToPlay()) {
                playCellByAi()
            }

            if (state.value.gamePlayersType == GamePlayersType.PvP) {
                changePlayer()
            }

            if (state.value.lastPlayedCells.isEmpty()) {
                val first = state.value.firstPlayer
                val second = state.value.secondPlayer

                _state.update {
                    it.copy(currentPlayer = if (first.diceIndex > second.diceIndex) first else second)
                }
                if (isAiTurnToPlay()) {
                    playCellByAi()
                }
            }
        }
    }

    private fun isAiTurnToPlay(): Boolean {
        return state.value.gamePlayersType == GamePlayersType.PvC &&
                state.value.currentPlayer?.type == PlayerType.Computer &&
                !state.value.isGameFinished &&
                state.value.isGameStarted
    }

    private fun playCellByAi() {
        checkIfGameIsFinished()
        val cell = gameLogic?.ai?.play()
        if (cell != null) {
            changeCellOwner(cell)
        }
        checkIfGameIsFinished()
    }

    private fun checkIfGameIsFinished() {
        _state.update { it.copy(winner = findWinner()) }
        if (state.value.winner != null) {
            finishGame()
        }
        if (gameLogic?.isGameDrew() == true) {
            handleDrewGame()
        }
    }

    private fun finishGame() {
        _state.update {
            it.copy(
                isGameFinished = true,
                winnerCells = gameLogic?.winnerCells ?: mutableStateListOf()
            )
        }

        if (state.value.winner?.type == PlayerType.Human) {
            playHumanWinSoundEffect()
        } else {
            playLoseSoundEffect()
        }
    }

    private fun playLoseSoundEffect() {
        if (isSoundOn) {
            scope.launch { eventChannel.send(GameEvent.PlayLoseSound) }
        }
    }

    private fun playHumanWinSoundEffect() {
        if (isSoundOn) {
            scope.launch { eventChannel.send(GameEvent.PlayWinSound) }
        }
    }

    private fun findWinner(): Player? {
        return when (state.value.gameType) {
            GameType.Simple -> gameLogic?.findWinner()
        }
    }

    private fun handleDrewGame() {
        finishGame()
        _state.update { it.copy(isGameDraw = true) }

        playDrawSoundEffect()
    }

    private fun changeCellOwner(
        cell: DoozCell
    ) {
        if (isVibrationOn) {
            scope.launch { eventChannel.send(GameEvent.Vibrate) }
        }
        if (isSoundOn) {
            scope.launch { eventChannel.send(GameEvent.PlayPencilSound) }
        }

        if (cell.owner == null && state.value.isGameStarted) {
            cell.owner = state.value.currentPlayer

            val cellIndex = state.value.gameCells.indexOf(cell)
            state.value.gameCells.removeAt(cellIndex)
            state.value.gameCells.add(cellIndex, cell)

            state.value.lastPlayedCells.add(cell)
            changePlayer()
        }
    }

    private fun changePlayer() {
        if (state.value.currentPlayer == state.value.firstPlayer) {
            _state.update { it.copy(currentPlayer = state.value.secondPlayer) }
        } else {
            _state.update { it.copy(currentPlayer = state.value.firstPlayer) }
        }
    }

    private fun playDrawSoundEffect() {
        if (isSoundOn) {
            scope.launch { eventChannel.send(GameEvent.PlayDrawSound) }
        }
    }

    private suspend fun prepareGame() {
        resetGame()
        prepareGameRules()
        preparePlayers()
        prepareGameLogic()
    }

    private suspend fun dummyDiceRolling() {
        if (isSoundOn) {
            scope.launch { eventChannel.send(GameEvent.PlayDrawSound) }
        }
        if (isVibrationOn) {
            scope.launch { eventChannel.send(GameEvent.PlayDrawSound) }
        }
        _state.update { it.copy(isRollingDices = true) }

        val firstPlayerDice = state.value.firstPlayer.diceIndex
        val secondPlayerDice = state.value.secondPlayer.diceIndex

        repeat(5) {
            _state.update {
                it.copy(
                    firstPlayer = state.value.firstPlayer.copy(diceIndex = Random.nextInt(1..6)),
                    secondPlayer = state.value.secondPlayer.copy(diceIndex = Random.nextInt(1..6)),
                )
            }
            delay(100.milliseconds)
        }
        _state.update {
            it.copy(
                firstPlayer = state.value.firstPlayer.copy(diceIndex = firstPlayerDice),
                secondPlayer = state.value.secondPlayer.copy(diceIndex = secondPlayerDice)
            )
        }
        delay(100.milliseconds)

        delay(500.milliseconds)
        _state.update { it.copy(isRollingDices = false) }
    }


    private fun resetGame() {
        _state.update {
            it.copy(
                winner = null,
                isGameFinished = false,
                isGameStarted = false,
                isGameDraw = false,
                lastPlayedCells = mutableStateListOf(),
                gameCells = getEmptyBoard(),
                winnerCells = emptyList()
            )
        }
    }

    private fun getEmptyBoard(): SnapshotStateList<DoozCell> {
        val columns = mutableStateListOf<SnapshotStateList<DoozCell>>()
        for (x in 0 until state.value.gameSize) {
            val row = mutableStateListOf<DoozCell>()
            for (y in 0 until state.value.gameSize)
                row.add(DoozCell(x, y))
            columns.add(row)
        }
        return columns.flatten().toMutableStateList()
    }

    private suspend fun prepareGameRules() {
        _state.update {
            it.copy(
                gameSize = settings.getGameSize(),
                gamePlayersType = settings.getGamePlayersType(),
                aiDifficulty = settings.getAiDifficulty(),
                firstPlayerPolicy = settings.getFirstPlayerPolicy()
            )
        }
    }

    private fun prepareGameLogic() {
        when (state.value.gameType) {
            GameType.Simple -> {
                gameLogic = SimpleGameLogic(
                    gameCells = state.value.gameCells.to3D(),
                    gameSize = state.value.gameSize,
                    aiDifficulty = state.value.aiDifficulty
                )
            }
        }
    }

    private suspend fun preparePlayers() {
        val firstPlayerName = settings.getFirstPlayerName()
        val secondPlayerName = settings.getSecondPlayerName()

        val firstPlayerShape = settings.getFirstPlayerShape().toShape() ?: XShape
        val secondPlayerShape = settings.getSecondPlayerName().toShape() ?: RingShape

        val firstPlayerDice = Random.nextInt(Constants.diceRange)
        val secondPlayerDice = Random.nextInt(Constants.diceRange)

        _state.update {
            it.copy(
                firstPlayer = state.value.firstPlayer.copy(
                    name = firstPlayerName,
                    shape = firstPlayerShape.toName(),
                    diceIndex = firstPlayerDice
                )
            )
        }
        val isPvC = state.value.gamePlayersType == GamePlayersType.PvC
        _state.update {
            it.copy(
                secondPlayer = state.value.secondPlayer.copy(
                    name = if (isPvC) PlayerType.Computer.name else secondPlayerName,
                    shape = secondPlayerShape.toName(),
                    type = if (isPvC) PlayerType.Computer else PlayerType.Human,
                    diceIndex = secondPlayerDice
                )
            )
        }

        if (state.value.firstPlayerPolicy == FirstPlayerPolicy.DiceRolling) {
            setFirstPlayerToDiceWinner()
        } else {
            _state.update { it.copy(currentPlayer = state.value.firstPlayer) }
        }
    }

    private fun setFirstPlayerToDiceWinner() {
        if (state.value.firstPlayer.diceIndex >= state.value.secondPlayer.diceIndex) {
            _state.update { it.copy(currentPlayer = state.value.firstPlayer) }
        } else {
            _state.update { it.copy(currentPlayer = state.value.secondPlayer) }
        }
    }
}