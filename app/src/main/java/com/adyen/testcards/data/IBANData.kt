package com.adyen.testcards.data

import com.adyen.testcards.domain.IBAN
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IBANData(
    val iban: String,
    @Json(name = "name") val holderName: String,
    @Json(name = "country") val issuingCountry: String,
) {

    fun toDomain() = IBAN(
        iban = iban,
        holderName = holderName,
        issuingCountry = issuingCountry,
        isFavorite = false,
    )
}
