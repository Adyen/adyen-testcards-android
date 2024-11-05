package com.adyen.testcards.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.tooling.preview.Preview
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UPI(
    upi: UPI,
    onFavoriteClick: (UPI, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((UPI) -> Unit)? = null,
) {
    var isFavorite by remember(upi) { mutableStateOf(upi.isFavorite) }
    var showCopyMenu by remember(upi) { mutableStateOf(false) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(upi, it)
        },
        icon = R.drawable.ic_pm_upi.takeIf { upi.showIcon },
        modifier = modifier
            .combinedClickable(
                onClick = { onClick?.invoke(upi) },
                onLongClick = { showCopyMenu = true },
            )
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Text(text = upi.virtualPaymentAddress)

        CopyMenu(
            expanded = showCopyMenu,
            onDismissRequest = { showCopyMenu = false },
            items = buildMap {
                set("Virtual Payment Address", upi.virtualPaymentAddress)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UPIPreview() {
    val upi = UPI(
        virtualPaymentAddress = "testvpa@icici",
        isFavorite = true,
        showIcon = true,
    )
    UPI(upi, { _, _ -> })
}
