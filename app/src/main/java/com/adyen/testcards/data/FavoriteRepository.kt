package com.adyen.testcards.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

internal interface FavoriteRepository {

    fun getFavorites(): Flow<FavoriteData>

    suspend fun storeCreditCard(id: String)

    suspend fun removeCreditCard(id: String)

    suspend fun storeGiftCard(id: String)

    suspend fun removeGiftCard(id: String)

    suspend fun storeIBAN(id: String)

    suspend fun removeIBAN(id: String)

    suspend fun storeUPI(id: String)

    suspend fun removeUPI(id: String)

    suspend fun storeUsernamePassword(id: String)

    suspend fun removeUsernamePassword(id: String)
}

@Singleton
internal class DefaultFavoriteRepository @Inject constructor(
    private val favoritesDataStore: DataStore<StoredFavorites>,
) : FavoriteRepository {

    override fun getFavorites(): Flow<FavoriteData> = favoritesDataStore.data
        .map {
            FavoriteData(
                creditCards = it.creditCardsList.toSet(),
                giftCards = it.giftCardsList.toSet(),
                ibans = it.ibansList.toSet(),
                upis = it.upisList.toSet(),
                usernamePasswords = it.usernamePasswordsList.toSet(),
            )
        }

    override suspend fun storeCreditCard(id: String) {
        favoritesDataStore.updateData { preferences ->
            preferences.toBuilder()
                .addCreditCards(id)
                .build()
        }
    }

    override suspend fun removeCreditCard(id: String) {
        favoritesDataStore.updateData { preferences ->
            val filtered = preferences.creditCardsList.filterNot { it == id }
            preferences.toBuilder()
                .clearCreditCards()
                .addAllCreditCards(filtered)
                .build()
        }
    }

    override suspend fun storeGiftCard(id: String) {
        favoritesDataStore.updateData { preferences ->
            preferences.toBuilder()
                .addGiftCards(id)
                .build()
        }
    }

    override suspend fun removeGiftCard(id: String) {
        favoritesDataStore.updateData { preferences ->
            val filtered = preferences.giftCardsList.filterNot { it == id }
            preferences.toBuilder()
                .clearGiftCards()
                .addAllGiftCards(filtered)
                .build()
        }
    }

    override suspend fun storeIBAN(id: String) {
        favoritesDataStore.updateData { preferences ->
            preferences.toBuilder()
                .addIbans(id)
                .build()
        }
    }

    override suspend fun removeIBAN(id: String) {
        favoritesDataStore.updateData { preferences ->
            val filtered = preferences.ibansList.filterNot { it == id }
            preferences.toBuilder()
                .clearIbans()
                .addAllIbans(filtered)
                .build()
        }
    }

    override suspend fun storeUPI(id: String) {
        favoritesDataStore.updateData { preferences ->
            preferences.toBuilder()
                .addUpis(id)
                .build()
        }
    }

    override suspend fun removeUPI(id: String) {
        favoritesDataStore.updateData { preferences ->
            val filtered = preferences.upisList.filterNot { it == id }
            preferences.toBuilder()
                .clearUpis()
                .addAllUpis(filtered)
                .build()
        }
    }

    override suspend fun storeUsernamePassword(id: String) {
        favoritesDataStore.updateData { preferences ->
            preferences.toBuilder()
                .addUsernamePasswords(id)
                .build()
        }
    }

    override suspend fun removeUsernamePassword(id: String) {
        favoritesDataStore.updateData { preferences ->
            val filtered = preferences.usernamePasswordsList.filterNot { it == id }
            preferences.toBuilder()
                .clearUsernamePasswords()
                .addAllUsernamePasswords(filtered)
                .build()
        }
    }
}
