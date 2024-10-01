package com.adyen.testcards.domain

import androidx.annotation.DrawableRes

data class CreditCardGroup(
    val group: String,
    @DrawableRes val icon: Int,
    val items: List<CreditCard>,
)

data class CreditCard(
    val number: String,
    val expiryDate: String,
    val securityCode: String,
    val issuingCountry: String,
    val is3DS: Boolean,
    val isFavorite: Boolean,
) {

    fun toSearchString(): String = "$number $expiryDate $securityCode $issuingCountry"
}
