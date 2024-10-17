package com.adyen.testcards.data

import com.adyen.testcards.domain.GiftCard
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiftCardData(
    @Json(name = "cardnumber") val number: String,
    @Json(name = "code") val securityCode: String,
    @Json(name = "type") val type: String,
    @Json(name = "logo") val logo: String,
) {

    fun toDomain() = GiftCard(
        number = number,
        securityCode = securityCode,
        type = type,
        logo = logo,
    )
}
