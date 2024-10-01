package com.adyen.testcards.domain

import android.service.autofill.Dataset
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.adyen.testcards.autofill.ParsedStructure
import javax.inject.Inject

@Suppress("DEPRECATION")
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
            val emptyPresentation = RemoteViews(applicationId, android.R.layout.simple_list_item_1)
            creditCardNumberId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(card.number), emptyPresentation)
            }
            creditCardExpiryDateId?.let {
                datasetBuilder.setValue(
                    it,
                    AutofillValue.forText(card.expiryDate),
                    emptyPresentation,
                )
            }
            creditCardSecurityCodeId?.let {
                datasetBuilder.setValue(
                    it,
                    AutofillValue.forText(card.securityCode),
                    emptyPresentation,
                )
            }
        }

        return datasetBuilder.build()
    }

    private fun createGiftCardDataset(card: GiftCard, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            val emptyPresentation = RemoteViews(applicationId, android.R.layout.simple_list_item_1)
            giftCardNumberId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(card.number), emptyPresentation)
            }
            giftCardPinId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(card.securityCode), emptyPresentation)
            }
        }

        return datasetBuilder.build()
    }

    private fun createIBANDataset(iban: IBAN, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            val emptyPresentation = RemoteViews(applicationId, android.R.layout.simple_list_item_1)
            ibanId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(iban.iban), emptyPresentation)
            }
        }

        return datasetBuilder.build()
    }

    private fun createUPIDataset(upi: UPI, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            val emptyPresentation = RemoteViews(applicationId, android.R.layout.simple_list_item_1)
            upiVpaId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(upi.virtualPaymentAddress), emptyPresentation)
            }
        }

        return datasetBuilder.build()
    }

    private fun createUsernamePasswordDataset(data: UsernamePassword, parsedStructure: ParsedStructure): Dataset {
        val datasetBuilder = Dataset.Builder()

        with(parsedStructure) {
            val emptyPresentation = RemoteViews(applicationId, android.R.layout.simple_list_item_1)
            usernameId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(data.username), emptyPresentation)
            }
            passwordId?.let {
                datasetBuilder.setValue(it, AutofillValue.forText(data.password), emptyPresentation)
            }
        }

        return datasetBuilder.build()
    }
}
