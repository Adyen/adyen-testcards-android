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
import com.adyen.testcards.domain.UsernamePassword

internal fun LazyListScope.usernamePasswordSection(
    usernamePasswords: List<UsernamePassword>,
    onFavoriteClick: (UsernamePassword, Boolean) -> Unit,
    onItemClick: ((UsernamePassword) -> Unit)? = null,
) {
    item(key = "Username password") {
        PaymentMethodTitle("Username password", R.drawable.ic_pm_wallet, Modifier.animateItem())
    }

    items(usernamePasswords, key = { it.username + it.type }) { item ->
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

@Composable
internal fun UsernamePassword(
    data: UsernamePassword,
    onFavoriteClick: (UsernamePassword, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((UsernamePassword) -> Unit)? = null,
) {
    var isFavorite by remember(data) { mutableStateOf(data.isFavorite) }
    FavoritableRow(
        isFavorite = isFavorite,
        onFavoriteClicked = {
            isFavorite = it
            onFavoriteClick(data, it)
        },
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke(data) }
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Column {
            Text(text = data.username)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = data.type, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = data.password, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
