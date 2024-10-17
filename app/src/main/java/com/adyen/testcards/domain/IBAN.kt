package com.adyen.testcards.domain

data class IBAN(
    val iban: String,
    val holderName: String,
    val issuingCountry: String,
    val isFavorite: Boolean = false,
    val showIcon: Boolean = false,
) {

    fun toSearchString(): String = "$iban $holderName $issuingCountry"
}
