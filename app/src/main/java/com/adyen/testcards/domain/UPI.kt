package com.adyen.testcards.domain

data class UPI(
    val virtualPaymentAddress: String,
    val isFavorite: Boolean = false,
    val showIcon: Boolean = false,
) {

    fun toSearchString(): String = virtualPaymentAddress
}
