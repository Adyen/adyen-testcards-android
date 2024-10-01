package com.adyen.testcards.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

internal interface PaymentMethodDataSource {

    suspend fun getCreditCards(): List<CreditCardGroupData>

    suspend fun getGiftCards(): List<GiftCardData>

    suspend fun getIBANs(): List<IBANData>

    suspend fun getUPIs(): List<UPIData>

    suspend fun getUsernamePasswords(): List<UsernamePasswordData>
}

internal class RemotePaymentMethodDataSource @Inject constructor(
    private val paymentMethodService: PaymentMethodService,
) : PaymentMethodDataSource {

    override suspend fun getCreditCards(): List<CreditCardGroupData> = paymentMethodService.getCards()

    override suspend fun getGiftCards(): List<GiftCardData> = paymentMethodService.getGiftCards()

    override suspend fun getIBANs(): List<IBANData> = paymentMethodService.getIBANs()

    override suspend fun getUPIs(): List<UPIData> {
        error("UPIs cannot be retrieved from service yet.")
    }

    override suspend fun getUsernamePasswords(): List<UsernamePasswordData> {
        error("Username passwords cannot be retrieved from service yet.")
    }
}

internal class LocalPaymentMethodDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val moshi: Moshi,
) : PaymentMethodDataSource {

    override suspend fun getCreditCards(): List<CreditCardGroupData> {
        error("Credit cards should be fetched from a remote data source.")
    }

    override suspend fun getGiftCards(): List<GiftCardData> {
        error("Gift cards should be fetched from a remote data source.")
    }

    override suspend fun getIBANs(): List<IBANData> {
        error("IBANs should be fetched from a remote data source.")
    }

    override suspend fun getUPIs(): List<UPIData> = withContext(Dispatchers.IO) {
        readJsonFile("upis.json", UPIData::class.java)
    }

    override suspend fun getUsernamePasswords(): List<UsernamePasswordData> = withContext(Dispatchers.IO) {
        readJsonFile("usernamepasswords.json", UsernamePasswordData::class.java)
    }

    private fun <T> readJsonFile(fileName: String, type: Class<T>): List<T> {
        val stringBuilder = StringBuilder()
        applicationContext.assets.open(fileName).use { stream ->
            val reader = BufferedReader(InputStreamReader(stream))
            reader.forEachLine { stringBuilder.append(it) }
        }

        val typedList = Types.newParameterizedType(List::class.java, type)
        return moshi.adapter<List<T>>(typedList).fromJson(stringBuilder.toString())!!
    }
}
