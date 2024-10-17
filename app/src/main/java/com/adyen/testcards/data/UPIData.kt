package com.adyen.testcards.data

import com.adyen.testcards.domain.UPI
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UPIData(
    val virtualPaymentAddress: String,
) {

    fun toDomain() = UPI(
        virtualPaymentAddress = virtualPaymentAddress,
    )
}
