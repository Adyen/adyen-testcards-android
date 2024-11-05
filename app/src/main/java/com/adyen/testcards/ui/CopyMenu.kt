package com.adyen.testcards.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Icon(ImageVector.vectorResource(R.drawable.ic_copy), null)
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Copy...",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

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
