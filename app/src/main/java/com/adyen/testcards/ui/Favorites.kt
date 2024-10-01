package com.adyen.testcards.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adyen.testcards.domain.CreditCard
import com.adyen.testcards.domain.GiftCard
import com.adyen.testcards.domain.IBAN
import com.adyen.testcards.domain.UPI
import com.adyen.testcards.domain.UsernamePassword

fun LazyListScope.favoritesSection(
    creditCards: List<CreditCard>,
    giftCards: List<GiftCard>,
    ibans: List<IBAN>,
    upis: List<UPI>,
    usernamePasswords: List<UsernamePassword>,
    onFavoriteClick: (Any, Boolean) -> Unit,
    onItemClick: ((Any) -> Unit)? = null,
) {
    item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp, 28.dp),
            )
            Spacer(Modifier.padding(8.dp))
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }

    items(creditCards, key = { "fav" + it.number }) { card ->
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

    items(giftCards, key = { "fav" + it.number }) {
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

    items(ibans, key = { "fav" + it.iban }) { iban ->
        HorizontalDivider(
            modifier = Modifier
                .animateItem()
                .padding(16.dp, 0.dp),
        )
        IBAN(
            iban = iban,
            onFavoriteClick = onFavoriteClick,
            onClick = onItemClick,
            modifier = Modifier.animateItem(),
        )
    }

    items(upis, key = { "fav" + it.virtualPaymentAddress }) { item ->
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

    items(usernamePasswords, key = { "fav" + it.username + it.type }) { item ->
        HorizontalDivider(
            modifier = Modifier
                .animateItem()
                .padding(16.dp, 0.dp),
        )
        UsernamePassword(
            data = item,
            onFavoriteClick = onFavoriteClick,
            onClick = onItemClick,
            modifier = Modifier.animateItem(),
        )
    }
}
