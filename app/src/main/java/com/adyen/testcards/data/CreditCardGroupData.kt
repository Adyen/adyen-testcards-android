package com.adyen.testcards.data

import com.adyen.testcards.R
import com.adyen.testcards.domain.CreditCard
import com.adyen.testcards.domain.CreditCardGroup
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditCardGroupData(
    val group: String,
    val logo: String,
    val items: List<CreditCardData>,
) {

    fun toDomain() = CreditCardGroup(
        group = group,
        icon = LogoMapping.getResourceId(logo) ?: R.drawable.ic_pm_card,
        items = items.map { it.toDomain() },
    )
}

@JsonClass(generateAdapter = true)
data class CreditCardData(
    @Json(name = "cardnumber") val number: String,
    @Json(name = "expiry") val expiryDate: String,
    @Json(name = "CVC") val securityCode: String,
    @Json(name = "country") val issuingCountry: String,
    @Json(name = "secure3DS") val is3DS: Boolean = false,
) {

    fun toDomain() = CreditCard(
        number = number,
        expiryDate = expiryDate,
        securityCode = securityCode.takeIfNotNA(),
        issuingCountry = issuingCountry.takeIfNotNA(),
        is3DS = is3DS,
    )
}

private fun String.takeIfNotNA(): String? {
    return takeIf { it != NA_STRING }
}

private const val NA_STRING = "n/a"
