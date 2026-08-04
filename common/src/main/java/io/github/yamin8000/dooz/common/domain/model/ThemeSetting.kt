package io.github.yamin8000.dooz.common.domain.model

import androidx.annotation.StringRes
import io.github.yamin8000.dooz.common.R

enum class ThemeSetting(
    @field:StringRes val persianNameStringResource: Int
) {
    Dark(R.string.theme_dark), Light(R.string.theme_light), System(R.string.theme_system);
}