package io.github.yamin8000.dooz.common.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SingleLinePersianText(
    text: String,
    modifier: Modifier = Modifier
) {
    AppText(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.basicMarquee()
    )
}