package com.adyen.testcards.ui

import android.content.ClipData
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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

        val clipboardManager: Clipboard = LocalClipboard.current
        items.forEach { (title, value) ->
            val coroutineScope = rememberCoroutineScope()
            DropdownMenuItem(
                text = { Text(title) },
                onClick = {
                    val clipData = ClipData.newPlainText(title, value)
                    coroutineScope.launch { clipboardManager.setClipEntry(ClipEntry(clipData)) }
                    onDismissRequest()
                },
            )
        }
    }
}
