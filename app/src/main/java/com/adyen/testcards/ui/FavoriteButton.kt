package com.adyen.testcards.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.tooling.preview.Preview
import com.adyen.testcards.ui.theme.AdyenTheme

@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val checkedState = rememberUpdatedState(newValue = isFavorite)
    IconToggleButton(checked = checkedState.value, onCheckedChange = onClick) {
        val image = if (checkedState.value) {
            Icons.Filled.Favorite
        } else {
            Icons.Default.FavoriteBorder
        }
        Icon(imageVector = image, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonPreview() {
    AdyenTheme {
        Column {
            FavoriteButton(isFavorite = true) {}
            FavoriteButton(isFavorite = false) {}
        }
    }
}
