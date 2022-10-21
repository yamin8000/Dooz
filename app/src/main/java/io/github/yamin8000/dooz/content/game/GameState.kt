/*
 *     Dooz
 *     GameState.kt Created by Yamin Siahmargooei at 2022/8/26
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

package io.github.yamin8000.dooz.content.game

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.game.GameConstants.gameDefaultSize
import io.github.yamin8000.dooz.game.logic.GameLogic
import io.github.yamin8000.dooz.game.logic.SimpleGameLogic
import io.github.yamin8000.dooz.model.*
import io.github.yamin8000.dooz.ui.RingShape
import io.github.yamin8000.dooz.ui.XShape
import io.github.yamin8000.dooz.ui.toName
import io.github.yamin8000.dooz.ui.toShape
import io.github.yamin8000.dooz.util.Constants
import io.github.yamin8000.dooz.util.Constants.aiPlayDelayRange
import io.github.yamin8000.dooz.util.DataStoreHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

class GameState(
    private val hapticFeedback: HapticFeedback,
    private val context: Context,
    private val scope: LifecycleCoroutineScope,
    var gameCells: MutableState<List<List<DoozCell>>>,
    val gameSize: MutableState<Int>,
    var currentPlayer: MutableState<Player?>,
    var players: MutableState<List<Player>>,
    var gamePlayersType: MutableState<GamePlayersType>,
    var isGameStarted: MutableState<Boolean>,
    var isGameFinished: MutableState<Boolean>,
    var winner: MutableState<Player?>,
    private var gameType: MutableState<GameType>,
    var isGameDrew: MutableState<Boolean>,
    var winnerCells: MutableState<List<DoozCell>>,
    val aiDifficulty: MutableState<AiDifficulty>,
    val isRollingDices: MutableState<Boolean>,
    var firstPlayerPolicy: MutableState<FirstPlayerPolicy>,
    var lastPlayedCells: MutableState<List<DoozCell>>,
) {
    private var isSoundOn = true
    private var isVibrationOn = true

    private var gameLogic: GameLogic? = null

    private val dataStore = DataStoreHelper(context.settings)

    init {
        scope.launch {
            isSoundOn = dataStore.getBoolean(Constants.isSoundOn) ?: true
            isVibrationOn = dataStore.getBoolean(Constants.isVibrationOn) ?: true
            prepareGame()
        }
    }

    fun newGame() {
        scope.launch {
            prepareGame()

            isGameStarted.value = true

            if (firstPlayerPolicy.value == FirstPlayerPolicy.DiceRolling)
                dummyDiceRolling()

            if (isAiTurnToPlay())
                scope.launch { playCellByAi() }
        }
    }

    private suspend fun prepareGame() {
        resetGame()
        prepareGameRules()
        preparePlayers()
        prepareGameLogic()
    }

    private fun resetGame() {
        winner.value = null
        isGameFinished.value = false
        isGameStarted.value = false
        isGameDrew.value = false
        lastPlayedCells.value = listOf()
        gameCells.value = getEmptyBoard()
        winnerCells.value = listOf()
    }

    fun playCell(
        cell: DoozCell
    ) {
        checkIfGameIsFinished()
        changeCellOwner(cell)
        checkIfGameIsFinished()

        if (isAiTurnToPlay())
            scope.launch { asyncPlayCellByAi() }
    }

    private fun playCellByAi() {
        checkIfGameIsFinished()
        val cell = gameLogic?.ai?.play()
        if (cell != null) changeCellOwner(cell)
        checkIfGameIsFinished()
    }

    private suspend fun asyncPlayCellByAi() {
        delay(Random.nextLong(aiPlayDelayRange))
        playCellByAi()
    }

    private fun isAiTurnToPlay(): Boolean {
        return gamePlayersType.value == GamePlayersType.PvC &&
                currentPlayer.value?.type == PlayerType.Computer &&
                !isGameFinished.value &&
                isGameStarted.value
    }

    private suspend fun prepareGameRules() {
        gameSize.value = dataStore.getInt(Constants.gameSize) ?: gameDefaultSize
        gamePlayersType.value = GamePlayersType.valueOf(
            dataStore.getString(Constants.gamePlayersType) ?: GamePlayersType.PvC.name
        )
        aiDifficulty.value = AiDifficulty.valueOf(
            dataStore.getString(Constants.aiDifficulty) ?: AiDifficulty.Easy.name
        )
        firstPlayerPolicy.value = FirstPlayerPolicy.valueOf(
            dataStore.getString(Constants.firstPlayerPolicy) ?: FirstPlayerPolicy.DiceRolling.name
        )
    }

    private fun prepareGameLogic() {
        when (gameType.value) {
            GameType.Simple -> gameLogic =
                SimpleGameLogic(gameCells.value, gameSize.value, aiDifficulty.value)
        }
    }

    private fun getEmptyBoard(): List<List<DoozCell>> {
        val columns = mutableListOf<List<DoozCell>>()
        for (x in 0 until gameSize.value) {
            val row = mutableListOf<DoozCell>()
            for (y in 0 until gameSize.value)
                row.add(DoozCell(x, y))
            columns.add(row)
        }
        return columns
    }

    private suspend fun preparePlayers() {
        val firstPlayerName = dataStore.getString(Constants.firstPlayerName)
            ?: context.getString(R.string.first_player_default_name)
        val secondPlayerName = dataStore.getString(Constants.secondPlayerName)
            ?: context.getString(R.string.second_player_default_name)

        val firstPlayerShape =
            dataStore.getString(Constants.firstPlayerShape)?.toShape() ?: XShape
        val secondPlayerShape =
            dataStore.getString(Constants.secondPlayerShape)?.toShape() ?: RingShape

        val firstPlayerDice = Random.nextInt(Constants.diceRange)
        val secondPlayerDice = Random.nextInt(Constants.diceRange)

        players.value = createPlayers(
            firstPlayerName,
            firstPlayerShape,
            firstPlayerDice,
            secondPlayerShape,
            secondPlayerDice,
            secondPlayerName
        )

        if (firstPlayerPolicy.value == FirstPlayerPolicy.DiceRolling)
            setFirstPlayerToDiceWinner()
        else currentPlayer.value = players.value.first()
    }

    private fun setFirstPlayerToDiceWinner() {
        currentPlayer.value = players.value.reduce { first, second ->
            if (first.diceIndex >= second.diceIndex) first else second
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
        if (gamePlayersType.value == GamePlayersType.PvC) {
            add(
                Player(
                    name = context.getString(R.string.computer),
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

    private suspend fun dummyDiceRolling() {
        if (isSoundOn)
            MediaPlayer.create(context, R.raw.dice).start()
        if (isVibrationOn)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        isRollingDices.value = true

        val firstPlayerDice = players.value.first().diceIndex
        val secondPlayerDice = players.value.last().diceIndex

        repeat(5) {
            players.value = buildList {
                add(players.value.first().copy(diceIndex = Random.nextInt(1..6)))
                add(players.value.last().copy(diceIndex = Random.nextInt(1..6)))
            }
            delay(100)
        }
        players.value = buildList {
            add(players.value.first().copy(diceIndex = firstPlayerDice))
            add(players.value.last().copy(diceIndex = secondPlayerDice))
        }
        delay(100)

        delay(500)
        isRollingDices.value = false
    }

    fun getOwnerShape(
        owner: Player?
    ): Shape {
        return if (owner == players.value.first()) owner.shape?.toShape() ?: XShape
        else owner?.shape?.toShape() ?: RingShape
    }

    private fun changePlayer() {
        if (currentPlayer.value == players.value.first()) currentPlayer.value = players.value.last()
        else currentPlayer.value = players.value.first()
    }

    private fun changeCellOwner(
        cell: DoozCell
    ) {
        if (isVibrationOn)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        if (isSoundOn)
            MediaPlayer.create(context, R.raw.pencil).start()

        if (cell.owner == null && isGameStarted.value) {
            lastPlayedCells.value = buildList {
                addAll(lastPlayedCells.value)
                add(cell)
            }
            cell.owner = currentPlayer.value
            changePlayer()
        }
    }

    private fun checkIfGameIsFinished() {
        winner.value = findWinner()
        if (winner.value != null)
            finishGame()
        if (gameLogic?.isGameDrew() == true)
            handleDrewGame()
    }

    private fun handleDrewGame() {
        finishGame()
        isGameDrew.value = true
    }

    private fun finishGame() {
        isGameFinished.value = true
        winnerCells.value = gameLogic?.winnerCells ?: listOf()
    }

    private fun findWinner(): Player? {
        return when (gameType.value) {
            GameType.Simple -> gameLogic?.findWinner()
        }
    }

    fun undo() {
        if (lastPlayedCells.value.isNotEmpty()) {
            val last = lastPlayedCells.value.last()
            val mutatedRow = gameCells.value[last.x].toMutableList()
            mutatedRow[last.y] = last.copy(owner = null)
            val mutatedGameCells = gameCells.value.toMutableList()
            mutatedGameCells[last.x] = mutatedRow
            gameCells.value = mutatedGameCells

            lastPlayedCells.value = buildList {
                val new = lastPlayedCells.value.toMutableList()
                new.removeLast()
                addAll(new)
            }
            prepareGameLogic()
            winner.value = null
            isGameFinished.value = false
            isGameDrew.value = false
            winnerCells.value = emptyList()

            if (isAiTurnToPlay())
                playCellByAi()

            if (gamePlayersType.value == GamePlayersType.PvP)
                changePlayer()

            if (lastPlayedCells.value.isEmpty()) {
                val first = players.value.first()
                val second = players.value.last()

                currentPlayer.value = if (first.diceIndex > second.diceIndex) first else second
                if (isAiTurnToPlay())
                    playCellByAi()
            }
        }
    }
}

@Composable
fun rememberHomeState(
    hapticFeedback: HapticFeedback = LocalHapticFeedback.current,
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    doozCells: MutableState<List<List<DoozCell>>> = rememberSaveable { mutableStateOf(emptyList()) },
    gameSize: MutableState<Int> = rememberSaveable { mutableStateOf(gameDefaultSize) },
    currentPlayer: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    players: MutableState<List<Player>> = rememberSaveable { mutableStateOf(listOf()) },
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(
            GamePlayersType.PvC
        )
    },
    isGameStarted: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isGameFinished: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winner: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    gameType: MutableState<GameType> = rememberSaveable { mutableStateOf(GameType.Simple) },
    isGameDrew: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winnerCells: MutableState<List<DoozCell>> = rememberSaveable { mutableStateOf(emptyList()) },
    aiDifficulty: MutableState<AiDifficulty> = rememberSaveable { mutableStateOf(AiDifficulty.Easy) },
    isRollingDices: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    firstPlayerPolicy: MutableState<FirstPlayerPolicy> = rememberSaveable {
        mutableStateOf(
            FirstPlayerPolicy.DiceRolling
        )
    },
    lastPlayedCells: MutableState<List<DoozCell>> = rememberSaveable { mutableStateOf(listOf()) }
) = remember(
    hapticFeedback,
    context,
    coroutineScope,
    doozCells,
    gameSize,
    currentPlayer,
    players,
    gamePlayersType,
    isGameStarted,
    isGameFinished,
    winner,
    gameType,
    isGameDrew,
    winnerCells,
    aiDifficulty,
    isRollingDices,
    firstPlayerPolicy,
    lastPlayedCells
) {
    GameState(
        hapticFeedback,
        context,
        coroutineScope,
        doozCells,
        gameSize,
        currentPlayer,
        players,
        gamePlayersType,
        isGameStarted,
        isGameFinished,
        winner,
        gameType,
        isGameDrew,
        winnerCells,
        aiDifficulty,
        isRollingDices,
        firstPlayerPolicy,
        lastPlayedCells
    )
}