package io.github.yamin8000.dooz.domain.repository

import io.github.yamin8000.dooz.ui.settings.ThemeSetting

interface SettingsRepository {

    suspend fun getTheme(): ThemeSetting
    suspend fun setTheme(theme: ThemeSetting)
}