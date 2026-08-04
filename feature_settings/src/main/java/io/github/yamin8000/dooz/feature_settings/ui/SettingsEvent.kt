package io.github.yamin8000.dooz.feature_settings.ui

import io.github.yamin8000.dooz.common.domain.model.ThemeSetting

sealed interface SettingsEvent {
    data class OnThemeChanged(val theme: ThemeSetting) : SettingsEvent
}