package com.adyen.testcards.domain

data class UsernamePassword(
    val username: String,
    val password: String,
    val type: String,
    val isFavorite: Boolean,
) {

    fun toSearchString(): String = "$username $password $type"
}
