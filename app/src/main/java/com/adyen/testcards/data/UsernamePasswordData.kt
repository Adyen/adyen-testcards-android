package com.adyen.testcards.data

import com.adyen.testcards.domain.UsernamePassword
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsernamePasswordData(
    val username: String,
    val password: String,
    val type: String,
) {

    fun toDomain() = UsernamePassword(
        username = username,
        password = password,
        type = type,
        isFavorite = false,
    )
}
