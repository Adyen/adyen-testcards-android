package com.adyen.testcards.domain

data class IBAN(
    val iban: String,
    val holderName: String,
    val issuingCountry: String,
    val isFavorite: Boolean,
) {

    fun toSearchString(): String = "$iban $holderName $issuingCountry"
}
