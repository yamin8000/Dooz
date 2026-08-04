package io.github.yamin8000.dooz.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import io.github.yamin8000.dooz.common.ui.theme.AppTheme
import io.github.yamin8000.dooz.common.ui.theme.Sizes

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        var checked by remember { mutableStateOf(false) }
        SwitchWithText(
            onCheckedChange = { checked = it },
            checked = checked,
            caption = {
                AppText(text = if (checked) "On" else "Off")
            }
        )
    }
}

@Composable
fun SwitchWithText(
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean,
    caption: String,
    modifier: Modifier = Modifier
) {
    SwitchWithText(
        onCheckedChange = onCheckedChange,
        checked = checked,
        caption = { AppText(text = caption) },
        modifier = modifier
    )
}

@Composable
fun SwitchWithText(
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier,
    caption: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(Sizes.Large)
            .clickable(
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    caption()
                    Switch(
                        checked = checked,
                        onCheckedChange = null
                    )
                }
            )
        }
    )
}
