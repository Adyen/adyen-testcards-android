package com.adyen.testcards.domain

import com.adyen.testcards.data.FavoriteData
import com.adyen.testcards.data.FavoriteRepository
import com.adyen.testcards.data.PaymentMethodRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

internal class GetPaymentMethodsUseCase @Inject constructor(
    private val paymentMethodRepository: PaymentMethodRepository,
    favoriteRepository: FavoriteRepository,
) {

    private val paymentMethods = MutableSharedFlow<Result<PaymentMethods>>()

    private val combined: Flow<Result<CombinedPaymentMethods>> = combine(
        paymentMethods,
        favoriteRepository.getFavorites(),
    ) { paymentMethods, favoriteData ->
        paymentMethods.map { combinePaymentMethods(it, favoriteData) }
    }

    fun getPaymentMethods(): Flow<Result<CombinedPaymentMethods>> = combined

    suspend fun refresh() {
        paymentMethods.emit(fetchPaymentMethods())
    }

    private suspend fun fetchPaymentMethods(): Result<PaymentMethods> = supervisorScope {
        val defCreditCards = async { paymentMethodRepository.getCreditCards() }
        val defGiftCards = async { paymentMethodRepository.getGiftCards() }
        val defIBANs = async { paymentMethodRepository.getIBANs() }
        val defUPIs = async { paymentMethodRepository.getUPIs() }
        val defUsernamePasswords = async { paymentMethodRepository.getUsernamePasswords() }

        try {
            Result.success(
                PaymentMethods(
                    defCreditCards.await().map { it.toDomain() },
                    defGiftCards.await().map { it.toDomain() },
                    defIBANs.await().map { it.toDomain() },
                    defUPIs.await().map { it.toDomain() },
                    defUsernamePasswords.await().map { it.toDomain() },
                ),
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private fun combinePaymentMethods(paymentMethods: PaymentMethods, favorites: FavoriteData): CombinedPaymentMethods {
        val favoriteCreditCards = mutableListOf<CreditCard>()
        val creditCards = paymentMethods.creditCards.map { group ->
            val items = group.items.map {
                val isFavorite = favorites.creditCards.contains(it.number)
                it.copy(isFavorite = isFavorite).also { mapped ->
                    if (isFavorite) favoriteCreditCards.add(mapped.copy(icon = group.icon))
                }
            }
            group.copy(items = items)
        }

        val favoriteGiftCards = mutableListOf<GiftCard>()
        val giftCards = paymentMethods.giftCards.map {
            val isFavorite = favorites.giftCards.contains(it.number)
            it.copy(isFavorite = isFavorite).also { mapped ->
                if (isFavorite) favoriteGiftCards.add(mapped.copy(showIcon = true))
            }
        }

        val favoriteIBANs = mutableListOf<IBAN>()
        val ibans = paymentMethods.ibans.map {
            val isFavorite = favorites.ibans.contains(it.iban)
            it.copy(isFavorite = isFavorite).also { mapped ->
                if (isFavorite) favoriteIBANs.add(mapped.copy(showIcon = true))
            }
        }

        val favoriteUPIs = mutableListOf<UPI>()
        val upis = paymentMethods.upis.map {
            val isFavorite = favorites.upis.contains(it.virtualPaymentAddress)
            it.copy(isFavorite = isFavorite).also { mapped ->
                if (isFavorite) favoriteUPIs.add(mapped.copy(showIcon = true))
            }
        }

        val favoriteUsernamePasswords = mutableListOf<UsernamePassword>()
        val usernamePasswords = paymentMethods.usernamePasswords.map {
            val isFavorite = favorites.usernamePasswords.contains(it.username)
            it.copy(isFavorite = isFavorite).also { mapped ->
                if (isFavorite) favoriteUsernamePasswords.add(mapped.copy(showIcon = true))
            }
        }

        return CombinedPaymentMethods(
            favoriteCreditCards = favoriteCreditCards,
            creditCards = creditCards,
            favoriteGiftCards = favoriteGiftCards,
            giftCards = giftCards,
            favoriteIBANs = favoriteIBANs,
            ibans = ibans,
            favoriteUPIs = favoriteUPIs,
            upis = upis,
            favoriteUsernamePasswords = favoriteUsernamePasswords,
            usernamePasswords = usernamePasswords,
        )
    }
}
