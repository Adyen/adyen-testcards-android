package com.adyen.testcards.domain

import com.adyen.testcards.data.FavoriteRepository
import javax.inject.Inject

internal class FavoriteItemUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend fun favorite(item: Any) {
        when (item) {
            is CreditCard -> favoriteRepository.storeCreditCard(item.number)
            is GiftCard -> favoriteRepository.storeGiftCard(item.number)
            is IBAN -> favoriteRepository.storeIBAN(item.iban)
            is UPI -> favoriteRepository.storeUPI(item.virtualPaymentAddress)
            is UsernamePassword -> favoriteRepository.storeUsernamePassword(item.username)
        }
    }

    suspend fun unfavorite(item: Any) {
        when (item) {
            is CreditCard -> favoriteRepository.removeCreditCard(item.number)
            is GiftCard -> favoriteRepository.removeGiftCard(item.number)
            is IBAN -> favoriteRepository.removeIBAN(item.iban)
            is UPI -> favoriteRepository.removeUPI(item.virtualPaymentAddress)
            is UsernamePassword -> favoriteRepository.removeUsernamePassword(item.username)
        }
    }
}
