package com.adyen.testcards.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R

@Composable
internal fun FavoritableRow(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Boolean) -> Unit,
    @DrawableRes icon: Int?,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        if (icon != null) {
            PaymentMethodIcon(
                icon,
                Modifier.width(36.dp),
            )
        }

        Box(Modifier.weight(1F, true)) {
            content()
        }

        FavoriteButton(isFavorite, onFavoriteClicked)
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritableRowPreview() {
    FavoritableRow(
        isFavorite = true,
        onFavoriteClicked = {},
        icon = R.drawable.ic_pm_mastercard,
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Text("5454 5454 5454 5454")
    }
}
