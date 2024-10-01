package com.adyen.testcards.domain

data class UPI(
    val virtualPaymentAddress: String,
    val isFavorite: Boolean,
) {

    fun toSearchString(): String = virtualPaymentAddress
}
