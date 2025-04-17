package com.adyen.testcards.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdyenSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = SearchBarDefaults.windowInsets,
) {
    val focusManager = LocalFocusManager.current
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { focusManager.clearFocus() },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text("Search...") },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_adyen),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        },
        content = {},
        expanded = false,
        onExpandedChange = {},
        windowInsets = windowInsets,
        modifier = modifier,
    )
}
