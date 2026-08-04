package io.github.yamin8000.dooz.feature_game.ui

import androidx.compose.ui.graphics.Shape
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
import io.github.yamin8000.dooz.feature_game.domain.logic.GameLogic
import io.github.yamin8000.dooz.feature_game.domain.logic.SimpleGameLogic
import io.github.yamin8000.dooz.common.ui.components.toName
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    private val scope = CoroutineScope(
        SupervisorJob() + viewModelScope.coroutineContext + exceptionHandler
    )
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    private var _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var gameLogic: GameLogic? = null

    private var isSoundOn = true
    private var isVibrationOn = true

    init {

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

            }

            GameAction.Undo -> {
                if (state.value.gamePlayersType == GamePlayersType.PvC) {
                    //2 undo action
                } else {
                    //1 undo action}
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
                winnerCells = gameLogic?.winnerCells ?: emptyList()
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
            //val player = MediaPlayer.create(context, io.github.yamin8000.dooz.R.raw.lose)
            //player.start()
        }
    }

    private fun playHumanWinSoundEffect() {
        if (isSoundOn) {
            //val player = MediaPlayer.create(context, io.github.yamin8000.dooz.R.raw.win)
            //player.start()
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
            //hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        if (isSoundOn) {
            //MediaPlayer.create(context, io.github.yamin8000.dooz.R.raw.pencil).start()
        }

        if (cell.owner == null && state.value.isGameStarted) {
            _state.update {
                it.copy(
                    lastPlayedCells = buildList {
                        addAll(state.value.lastPlayedCells)
                        add(cell)
                    }
                )
            }
            cell.owner = state.value.currentPlayer
            changePlayer()
        }
    }

    private fun changePlayer() {
        if (state.value.currentPlayer == state.value.players.first()) {
            _state.update { it.copy(currentPlayer = state.value.players.last()) }
        } else {
            _state.update { it.copy(currentPlayer = state.value.players.first()) }
        }
    }

    private fun playDrawSoundEffect() {
        if (isSoundOn) {
            //val player = MediaPlayer.create(context, io.github.yamin8000.dooz.R.raw.draw)
            //player.start()
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
            //MediaPlayer.create(context, io.github.yamin8000.dooz.R.raw.dice).start()
        }
        if (isVibrationOn) {
            //hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        _state.update { it.copy(isRollingDices = true) }

        val firstPlayerDice = state.value.players.first().diceIndex
        val secondPlayerDice = state.value.players.last().diceIndex

        repeat(5) {
            /*players.value = buildList {
                add(players.value.first().copy(diceIndex = Random.nextInt(1..6)))
                add(players.value.last().copy(diceIndex = Random.nextInt(1..6)))
            }*/
            delay(100.milliseconds)
        }
        /*players.value = buildList {
            add(players.value.first().copy(diceIndex = firstPlayerDice))
            add(players.value.last().copy(diceIndex = secondPlayerDice))
        }*/
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
                lastPlayedCells = emptyList(),
                gameCells = getEmptyBoard(),
                winnerCells = emptyList()
            )
        }
    }

    private fun getEmptyBoard(): List<List<DoozCell>> {
        val columns = mutableListOf<List<DoozCell>>()
        for (x in 0 until state.value.gameSize) {
            val row = mutableListOf<DoozCell>()
            for (y in 0 until state.value.gameSize)
                row.add(DoozCell(x, y))
            columns.add(row)
        }
        return columns
    }

    private suspend fun prepareGameRules() {
        _state.update {
            it.copy(

            )
        }
        /*gameSize.intValue = dataStore.getInt(Constants.gameSize) ?: GAME_DEFAULT_SIZE
        gamePlayersType.value = GamePlayersType.valueOf(
            dataStore.getString(Constants.gamePlayersType) ?: GamePlayersType.PvC.name
        )
        aiDifficulty.value = AiDifficulty.valueOf(
            dataStore.getString(Constants.aiDifficulty) ?: AiDifficulty.Easy.name
        )
        firstPlayerPolicy.value = FirstPlayerPolicy.valueOf(
            dataStore.getString(Constants.firstPlayerPolicy) ?: FirstPlayerPolicy.DiceRolling.name
        )*/
    }

    private fun prepareGameLogic() {
        when (state.value.gameType) {
            GameType.Simple -> {
                gameLogic = SimpleGameLogic(
                    gameCells = state.value.gameCells,
                    gameSize = state.value.gameSize,
                    aiDifficulty = state.value.aiDifficulty
                )
            }
        }
    }

    private suspend fun preparePlayers() {
        /*val firstPlayerName = dataStore.getString(Constants.firstPlayerName)
            ?: context.getString(commonR.string.first_player_default_name)
        val secondPlayerName = dataStore.getString(Constants.secondPlayerName)
            ?: context.getString(commonR.string.second_player_default_name)

        val firstPlayerShape =
            dataStore.getString(Constants.firstPlayerShape)?.toShape() ?: XShape
        val secondPlayerShape =
            dataStore.getString(Constants.secondPlayerShape)?.toShape() ?: RingShape

        val firstPlayerDice = Random.nextInt(Constants.diceRange)
        val secondPlayerDice = Random.nextInt(Constants.diceRange)

        _state.update {
            it.copy(
                players = createPlayers(
                    firstPlayerName,
                    firstPlayerShape,
                    firstPlayerDice,
                    secondPlayerShape,
                    secondPlayerDice,
                    secondPlayerName
                )
            )
        }*/

        if (state.value.firstPlayerPolicy == FirstPlayerPolicy.DiceRolling) {
            setFirstPlayerToDiceWinner()
        } else {
            _state.update { it.copy(currentPlayer = state.value.players.first()) }
        }
    }

    private fun setFirstPlayerToDiceWinner() {
        _state.update {
            it.copy(currentPlayer = state.value.players.reduce { first, second ->
                if (first.diceIndex >= second.diceIndex) first else second
            })
        }
    }

    private fun createPlayers(
        firstPlayerName: String,
        firstPlayerShape: Shape,
        firstPlayerDice: Int,
        secondPlayerShape: Shape,
        secondPlayerDice: Int,
        secondPlayerName: String
    ) = buildList {
        add(Player(firstPlayerName, firstPlayerShape.toName(), diceIndex = firstPlayerDice))
        if (state.value.gamePlayersType == GamePlayersType.PvC) {
            add(
                Player(
                    name = PlayerType.Computer.name,
                    shape = secondPlayerShape.toName(),
                    type = PlayerType.Computer,
                    diceIndex = secondPlayerDice
                )
            )
        } else add(
            Player(
                secondPlayerName,
                secondPlayerShape.toName(),
                diceIndex = secondPlayerDice
            )
        )
    }
}