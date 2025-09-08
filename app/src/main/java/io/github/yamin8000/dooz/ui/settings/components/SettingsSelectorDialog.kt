package io.github.yamin8000.dooz.ui.settings.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.ui.components.PersianText
import io.github.yamin8000.dooz.ui.components.SingleLinePersianText

@Composable
internal fun <T> SettingsSelectorDialog(
    title: String,
    icon: (@Composable () -> Unit)? = null,
    displayProvider: (T, Context) -> String = { item, _ -> item.toString() },
    options: List<T>,
    currentItem: T,
    onDismiss: () -> Unit,
    onOptionChanged: (T) -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {/*ignored*/ },
        icon = icon,
        title = { SingleLinePersianText(title) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth(),
                content = {
                    options.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                2.dp,
                                Alignment.Start
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = item == currentItem,
                                    role = Role.RadioButton,
                                    onClick = {
                                        onOptionChanged(item)
                                        onDismiss()
                                    }
                                ),
                            content = {
                                RadioButton(
                                    selected = item == currentItem,
                                    onClick = null,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                PersianText(
                                    text = displayProvider(item, context),
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}