package com.adyen.testcards.domain

data class PaymentMethods(
    val creditCards: List<CreditCardGroup> = emptyList(),
    val giftCards: List<GiftCard> = emptyList(),
    val ibans: List<IBAN> = emptyList(),
    val upis: List<UPI> = emptyList(),
    val usernamePasswords: List<UsernamePassword> = emptyList(),
) {

    fun isEmpty(): Boolean = creditCards.isEmpty() &&
        giftCards.isEmpty() &&
        ibans.isEmpty() &&
        upis.isEmpty() &&
        usernamePasswords.isEmpty()
}

data class CombinedPaymentMethods(
    val favoriteCreditCards: List<CreditCard> = emptyList(),
    val favoriteGiftCards: List<GiftCard> = emptyList(),
    val favoriteIBANs: List<IBAN> = emptyList(),
    val favoriteUPIs: List<UPI> = emptyList(),
    val favoriteUsernamePasswords: List<UsernamePassword> = emptyList(),
    val creditCards: List<CreditCardGroup> = emptyList(),
    val giftCards: List<GiftCard> = emptyList(),
    val ibans: List<IBAN> = emptyList(),
    val upis: List<UPI> = emptyList(),
    val usernamePasswords: List<UsernamePassword> = emptyList(),
) {

    fun isEmpty(): Boolean = favoriteCreditCards.isEmpty() &&
        favoriteGiftCards.isEmpty() &&
        favoriteIBANs.isEmpty() &&
        favoriteUPIs.isEmpty() &&
        favoriteUsernamePasswords.isEmpty() &&
        creditCards.isEmpty() &&
        giftCards.isEmpty() &&
        ibans.isEmpty() &&
        upis.isEmpty() &&
        usernamePasswords.isEmpty()

    fun hasFavorites() = favoriteCreditCards.isNotEmpty() ||
        favoriteGiftCards.isNotEmpty() ||
        favoriteIBANs.isNotEmpty() ||
        favoriteUPIs.isNotEmpty() ||
        favoriteUsernamePasswords.isNotEmpty()
}
