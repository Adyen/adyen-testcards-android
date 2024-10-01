package com.adyen.testcards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun FavoritableRow(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Boolean) -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        content()

        FavoriteButton(isFavorite, onFavoriteClicked)
    }
}
