package com.adyen.testcards.domain

data class GiftCard(
    val number: String,
    val securityCode: String,
    val type: String,
    val logo: String,
    val isFavorite: Boolean,
) {

    fun toSearchString(): String = "$number $securityCode $type"
}