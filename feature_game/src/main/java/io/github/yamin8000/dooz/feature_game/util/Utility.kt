package io.github.yamin8000.dooz.feature_game.util

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

object Utility {

    @Composable
    fun LockScreenOrientation(orientation: Int) {
        val activity = LocalActivity.current
        DisposableEffect(Unit) {
            if (activity != null) {
                val originalOrientation = activity.requestedOrientation
                activity.requestedOrientation = orientation
                onDispose {
                    // restore original orientation when view disappears
                    activity.requestedOrientation = originalOrientation
                }
            }
            onDispose { }
        }
    }

    //simple 90 degrees rotation
    fun <T> List<List<T>>.rotated(): List<List<T>> {
        val rotated = mutableListOf<List<T>>()

        for (j in this.indices) {
            val newRow = mutableListOf<T>()
            for (i in this.indices)
                newRow.add(this[i][j])
            rotated.add(newRow.reversed())
        }

        return rotated
    }

    fun <T> List<List<T>>.diagonals(): Pair<List<T>, List<T>> {
        return this.diagonal() to this.rotated().diagonal()
    }

    private fun <T> List<List<T>>.diagonal(): List<T> {
        val diagonal = mutableListOf<T>()
        for (i in this.indices)
            diagonal.add(this[i][i])
        return diagonal
    }
}