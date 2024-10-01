package com.adyen.testcards.data

internal data class FavoriteData(
    val creditCards: Set<String>,
    val giftCards: Set<String>,
    val ibans: Set<String>,
    val upis: Set<String>,
    val usernamePasswords: Set<String>,
)
