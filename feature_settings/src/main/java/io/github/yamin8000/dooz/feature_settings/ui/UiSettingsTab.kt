package io.github.yamin8000.dooz.feature_settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Games
import androidx.compose.material.icons.twotone.People
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.feature_settings.domain.model.SettingsTab

data class UiSettingsTab(
    val index: Int,
    val name: String,
    val icon: ImageVector,
    val tab: SettingsTab
) {
    fun toDomain() = this.tab

    companion object {
        @Composable
        fun SettingsTab.toUi(): UiSettingsTab {
            return when (this) {
                SettingsTab.Game -> UiSettingsTab(
                    index = 0,
                    name = stringResource(R.string.game),
                    icon = Icons.TwoTone.Games,
                    tab = this
                )

                SettingsTab.General -> UiSettingsTab(
                    index = 0,
                    name = stringResource(R.string.general),
                    icon = Icons.TwoTone.Settings,
                    tab = this
                )

                SettingsTab.Players -> UiSettingsTab(
                    index = 0,
                    name = stringResource(R.string.players),
                    icon = Icons.TwoTone.People,
                    tab = this
                )
            }
        }
    }
}
