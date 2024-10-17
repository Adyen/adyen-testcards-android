package com.adyen.testcards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.adyen.testcards.domain.GiftCard

internal fun LazyListScope.giftCardSection(
    giftCards: List<GiftCard>,
    onFavoriteClick: (GiftCard, Boolean) -> Unit,
    onItemClick: ((GiftCard) -> Unit)? = null,
) {
    item(key = "Gift cards") {
        PaymentMethodTitle("Gift cards", R.drawable.ic_pm_gift_card, Modifier.animateItem())
    }

    items(giftCards, key = { it.number }) {
        HorizontalDivider(
            modifier = Modifier
                .animateItem()
                .padding(16.dp, 0.dp),
        )
        GiftCard(
            giftCard = it,
            onFavoriteClick = onFavoriteClick,
            onClick = onItemClick,
            modifier = Modifier.animateItem(),
        )
    }
}

@Composable
internal fun GiftCard(
    giftCard: GiftCard,
    onFavoriteClick: (GiftCard, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((GiftCard) -> Unit)? = null,
) {
    var isFavorite by remember(giftCard) { mutableStateOf(giftCard.isFavorite) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(giftCard, it)
        },
        icon = R.drawable.ic_pm_gift_card.takeIf { giftCard.showIcon },
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke(giftCard) }
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Column {
            Text(text = giftCard.number)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = giftCard.type, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = giftCard.securityCode, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GiftCardPreview() {
    val card = GiftCard(
        number = "1234 1234 1234 1234",
        securityCode = "123",
        type = "test",
        logo = "logo",
        isFavorite = true,
        showIcon = true,
    )
    GiftCard(card, { _, _ -> })
}
