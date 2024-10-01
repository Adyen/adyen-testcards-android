package com.adyen.testcards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R
import com.adyen.testcards.domain.CreditCard
import com.adyen.testcards.domain.CreditCardGroup

internal fun LazyListScope.creditCardSection(
    groups: List<CreditCardGroup>,
    onFavoriteClick: (CreditCard, Boolean) -> Unit,
    onItemClick: ((CreditCard) -> Unit)? = null,
) {
    item(key = "Credit cards") {
        PaymentMethodTitle("Credit cards", R.drawable.ic_pm_card, Modifier.animateItem())
    }

    groups.forEach { group ->
        item(key = group.group) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .animateItem()
                    .padding(16.dp),
            ) {
                Text(group.group, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.padding(4.dp))
                PaymentMethodIcon(resId = group.icon, modifier = Modifier.size(30.dp, Dp.Unspecified))
            }
        }

        items(group.items, key = { it.number }) { card ->
            HorizontalDivider(
                modifier = Modifier
                    .animateItem()
                    .padding(16.dp, 0.dp),
            )
            CreditCard(
                card = card,
                onFavoriteClick = onFavoriteClick,
                onClick = onItemClick,
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
internal fun CreditCard(
    card: CreditCard,
    onFavoriteClick: (CreditCard, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((CreditCard) -> Unit)? = null,
) {
    var isFavorite by remember(card) { mutableStateOf(card.isFavorite) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(card, it)
        },
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke(card) }
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Column {
            Text(text = card.number)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = card.expiryDate, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = card.securityCode, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = card.issuingCountry, style = MaterialTheme.typography.labelSmall)

                if (card.is3DS) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "3DS", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreditCardPreview() {
    val card = CreditCard("1234 1234 1234 1234", "03/30", "123", "NL", true, true)
    CreditCard(card = card, onFavoriteClick = { _, _ -> })
}
