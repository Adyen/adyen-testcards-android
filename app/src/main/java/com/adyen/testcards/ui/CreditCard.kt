package com.adyen.testcards.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
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
    var showCopyMenu by remember(card) { mutableStateOf(false) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(card, it)
        },
        icon = card.icon,
        modifier = modifier
            .combinedClickable(
                onClick = { onClick?.invoke(card) },
                onLongClick = { showCopyMenu = true },
            )
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Column {
            Text(text = card.number)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = card.expiryDate, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = card.securityCode ?: stringResource(R.string.cvc_not_applicable),
                    style = MaterialTheme.typography.labelSmall,
                )
                if (card.issuingCountry != null) {
                    Text(
                        text = card.issuingCountry,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

                if (card.is3DS) {
                    Text(text = "3DS", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        CopyMenu(
            expanded = showCopyMenu,
            onDismissRequest = { showCopyMenu = false },
            items = buildMap {
                set("Number", card.number)
                set("Expiry date", card.expiryDate)
                card.securityCode?.let { set("Security code", it) }
            },
        )
    }
}

@Preview(showBackground = true, name = "All params are set")
@Composable
private fun CreditCardPreview() {
    val card = CreditCard(
        number = "1234 1234 1234 1234",
        expiryDate = "03/30",
        securityCode = "123",
        issuingCountry = "NL",
        is3DS = true,
        isFavorite = true,
        icon = R.drawable.ic_pm_visa,
    )
    CreditCard(card = card, onFavoriteClick = { _, _ -> })
}

@Preview(showBackground = true, name = "All optional params are null")
@Composable
private fun CreditCardMinimalPreview() {
    val card = CreditCard(
        number = "1234 1234 1234 1234",
        expiryDate = "03/30",
        securityCode = null,
        issuingCountry = null,
        is3DS = false,
        isFavorite = false,
        icon = null,
    )
    CreditCard(card = card, onFavoriteClick = { _, _ -> })
}
