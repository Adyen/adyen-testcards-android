package com.adyen.testcards.data

import retrofit2.http.GET

internal interface PaymentMethodService {

    @GET("main/data/cards.json")
    suspend fun getCards(): List<CreditCardGroupData>

    @GET("main/data/giftcards.json")
    suspend fun getGiftCards(): List<GiftCardData>

    @GET("main/data/ibans.json")
    suspend fun getIBANs(): List<IBANData>
}
