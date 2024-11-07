package com.adyen.testcards.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun CopyMenu(
    expanded: Boolean,
    items: Map<String, String>,
    onDismissRequest: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            text = "Copy...",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 16.dp),
        )

        HorizontalDivider()

        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        items.forEach { (title, value) ->
            DropdownMenuItem(
                text = { Text(title) },
                onClick = {
                    clipboardManager.setText(AnnotatedString(value))
                    onDismissRequest()
                },
            )
        }
    }
}
