package com.adyen.testcards.domain

import android.os.Build
import android.service.autofill.Dataset
import android.service.autofill.Field
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import com.adyen.testcards.autofill.ParsedStructure
import javax.inject.Inject

internal class CreateDatasetUseCase @Inject constructor() {

    fun createDataset(input: Any, parsedStructure: ParsedStructure): Dataset = when (input) {
        is CreditCard -> createCreditCardDataset(input, parsedStructure)
        is GiftCard -> createGiftCardDataset(input, parsedStructure)
        is IBAN -> createIBANDataset(input, parsedStructure)
        is UPI -> createUPIDataset(input, parsedStructure)
        is UsernamePassword -> createUsernamePasswordDataset(input, parsedStructure)
        else -> error("Unknown input: $input")
    }

    private fun createCreditCardDataset(card: CreditCard, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            creditCardNumberId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(card.number.replaceSpaces()))
            }
            creditCardExpiryDateId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(card.expiryDate))
            }
            creditCardSecurityCodeId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(card.securityCode))
            }
        }

        return datasetBuilder.build()
    }

    private fun createGiftCardDataset(card: GiftCard, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            giftCardNumberId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(card.number.replaceSpaces()))
            }
            giftCardPinId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(card.securityCode))
            }
        }

        return datasetBuilder.build()
    }

    private fun createIBANDataset(iban: IBAN, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            ibanId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(iban.iban.replaceSpaces()))
            }
        }

        return datasetBuilder.build()
    }

    private fun createUPIDataset(upi: UPI, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            upiVpaId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(upi.virtualPaymentAddress))
            }
        }

        return datasetBuilder.build()
    }

    private fun createUsernamePasswordDataset(data: UsernamePassword, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            usernameId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(data.username))
            }
            passwordId?.let {
                datasetBuilder.setValueCompat(it, AutofillValue.forText(data.password))
            }
        }

        return datasetBuilder.build()
    }

    private fun Dataset.Builder.setValueCompat(
        id: AutofillId,
        autofillValue: AutofillValue?
    ): Dataset.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setField(
                id,
                autofillValue?.let {
                    Field.Builder()
                        .setValue(it)
                        .build()
                },
            )
        } else {
            @Suppress("DEPRECATION")
            setValue(id, autofillValue)
        }
        return this
    }

    private fun String.replaceSpaces(): String {
        return this.replace(" ", "")
    }
}
