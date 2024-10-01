package com.adyen.testcards.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentMethodTitle(
    title: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(16.dp),
    ) {
        PaymentMethodIcon(icon)
        Spacer(Modifier.padding(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}
