package com.adyen.testcards.data

import javax.inject.Inject
import javax.inject.Singleton

internal interface PaymentMethodRepository {

    suspend fun getCreditCards(): List<CreditCardGroupData>

    suspend fun getGiftCards(): List<GiftCardData>

    suspend fun getIBANs(): List<IBANData>

    suspend fun getUPIs(): List<UPIData>

    suspend fun getUsernamePasswords(): List<UsernamePasswordData>
}

@Singleton
internal class DefaultPaymentMethodRepository @Inject constructor(
    private val remotePaymentMethodDataSource: RemotePaymentMethodDataSource,
    private val localPaymentMethodDataSource: LocalPaymentMethodDataSource,
) : PaymentMethodRepository {

    override suspend fun getCreditCards(): List<CreditCardGroupData> = remotePaymentMethodDataSource.getCreditCards()

    override suspend fun getGiftCards(): List<GiftCardData> = remotePaymentMethodDataSource.getGiftCards()

    override suspend fun getIBANs(): List<IBANData> = remotePaymentMethodDataSource.getIBANs()

    override suspend fun getUPIs(): List<UPIData> = localPaymentMethodDataSource.getUPIs()

    override suspend fun getUsernamePasswords(): List<UsernamePasswordData> =
        localPaymentMethodDataSource.getUsernamePasswords()
}
