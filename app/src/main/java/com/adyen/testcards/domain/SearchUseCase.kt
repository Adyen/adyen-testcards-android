package com.adyen.testcards.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchUseCase @Inject constructor() {

    suspend fun searchCreditCards(
        query: String,
        creditCardGroups: List<CreditCardGroup>
    ): List<CreditCardGroup> = withContext(Dispatchers.Default) {
        // Search based on group name first
        val groupHits = creditCardGroups.filter { it.group.contains(query, ignoreCase = true) }

        if (groupHits.isNotEmpty()) {
            return@withContext groupHits
        }

        val itemHits = mutableListOf<CreditCardGroup>()
        creditCardGroups.forEach { group ->
            val filteredItems = group.items.filter { it.toSearchString().contains(query) }
            if (filteredItems.isNotEmpty()) {
                itemHits.add(group.copy(items = filteredItems))
            }
        }
        return@withContext itemHits
    }

    suspend fun searchGiftCards(
        query: String,
        giftCards: List<GiftCard>,
    ): List<GiftCard> = withContext(Dispatchers.Default) {
        if ("gift cards".contains(query, ignoreCase = true)) {
            return@withContext giftCards
        }

        return@withContext giftCards.filter { it.toSearchString().contains(query, ignoreCase = true) }
    }

    suspend fun searchIBANs(
        query: String,
        ibans: List<IBAN>,
    ): List<IBAN> = withContext(Dispatchers.Default) {
        if ("IBAN".contains(query, ignoreCase = true)) {
            return@withContext ibans
        }

        return@withContext ibans.filter { it.toSearchString().contains(query, ignoreCase = true) }
    }

    suspend fun searchUPIs(
        query: String,
        upis: List<UPI>,
    ): List<UPI> = withContext(Dispatchers.Default) {
        if ("UPI".contains(query, ignoreCase = true)) {
            return@withContext upis
        }

        return@withContext upis.filter { it.toSearchString().contains(query, ignoreCase = true) }
    }

    suspend fun searchUsernamePasswords(
        query: String,
        usernamePasswords: List<UsernamePassword>,
    ): List<UsernamePassword> = withContext(Dispatchers.Default) {
        if ("username password".contains(query, ignoreCase = true)) {
            return@withContext usernamePasswords
        }

        return@withContext usernamePasswords.filter { it.toSearchString().contains(query, ignoreCase = true) }
    }
}
