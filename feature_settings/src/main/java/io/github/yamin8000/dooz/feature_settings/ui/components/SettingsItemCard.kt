package io.github.yamin8000.dooz.feature_settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.components.DefaultCornerShape
import io.github.yamin8000.dooz.common.ui.theme.Sizes

@Composable
internal fun SettingsItemCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Sizes.Small),
        horizontalAlignment = Alignment.Start,
        content = {
            AppText(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = modifier,
                shape = DefaultCornerShape,
                content = {
                    Column(
                        modifier = Modifier.padding(Sizes.Large),
                        verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = content
                    )
                }
            )
        }
    )
}