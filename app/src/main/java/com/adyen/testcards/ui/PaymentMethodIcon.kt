package com.adyen.testcards.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adyen.testcards.R

@Composable
fun PaymentMethodIcon(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Image(
        imageVector = ImageVector.vectorResource(resId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(5.dp)),
    )
}

@Preview(showBackground = true)
@Composable
private fun PaymentMethodIconPreview() {
    Column {
        PaymentMethodIcon(R.drawable.ic_pm_bank)
        Spacer(Modifier.padding(4.dp))
        PaymentMethodIcon(R.drawable.ic_pm_visa)
    }
}
