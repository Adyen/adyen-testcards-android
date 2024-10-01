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
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R
import com.adyen.testcards.domain.IBAN

internal fun LazyListScope.ibanSection(
    ibans: List<IBAN>,
    onFavoriteClick: (IBAN, Boolean) -> Unit,
    onItemClick: ((IBAN) -> Unit)? = null,
) {
    item(key = "IBANs") {
        PaymentMethodTitle("IBANs", R.drawable.ic_pm_bank, Modifier.animateItem())
    }

    items(ibans, key = { it.iban }) { iban ->
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
}

@Composable
internal fun IBAN(
    iban: IBAN,
    onFavoriteClick: (IBAN, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((IBAN) -> Unit)? = null,
) {
    var isFavorite by remember(iban) { mutableStateOf(iban.isFavorite) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(iban, it)
        },
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke(iban) }
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Column {
            Text(text = iban.iban)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = iban.holderName, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = iban.issuingCountry, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
