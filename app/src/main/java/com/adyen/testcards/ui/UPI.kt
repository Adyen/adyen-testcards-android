package com.adyen.testcards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R
import com.adyen.testcards.domain.UPI

internal fun LazyListScope.upiSection(
    upis: List<UPI>,
    onFavoriteClick: (UPI, Boolean) -> Unit,
    onItemClick: ((UPI) -> Unit)? = null,
) {
    item(key = "UPI") {
        PaymentMethodTitle("UPI", R.drawable.ic_pm_upi, Modifier.animateItem())
    }

    items(upis, key = { it.virtualPaymentAddress }) { item ->
        HorizontalDivider(
            modifier = Modifier
                .animateItem()
                .padding(16.dp, 0.dp),
        )
        UPI(
            upi = item,
            onFavoriteClick = onFavoriteClick,
            onClick = onItemClick,
            modifier = Modifier.animateItem(),
        )
    }
}

@Composable
internal fun UPI(
    upi: UPI,
    onFavoriteClick: (UPI, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((UPI) -> Unit)? = null,
) {
    var isFavorite by remember(upi) { mutableStateOf(upi.isFavorite) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(upi, it)
        },
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke(upi) }
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Text(text = upi.virtualPaymentAddress)
    }
}
