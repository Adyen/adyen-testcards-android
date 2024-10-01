package com.adyen.testcards.autofill

import android.os.Parcelable
import android.view.autofill.AutofillId
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ParsedStructure(
    var applicationId: String? = null,
    var creditCardNumberId: AutofillId? = null,
    var creditCardExpiryDateId: AutofillId? = null,
    var creditCardSecurityCodeId: AutofillId? = null,
    var giftCardNumberId: AutofillId? = null,
    var giftCardPinId: AutofillId? = null,
    var ibanId: AutofillId? = null,
    var upiVpaId: AutofillId? = null,
    var usernameId: AutofillId? = null,
    var usernameCandidate: AutofillId? = null,
    var passwordId: AutofillId? = null,
) : Parcelable {

    fun isValid() = allIds().isNotEmpty()

    fun allIds() = listOfNotNull(
        creditCardNumberId,
        creditCardExpiryDateId,
        creditCardSecurityCodeId,
        giftCardNumberId,
        giftCardPinId,
        upiVpaId,
        usernameId,
        passwordId,
    )
}
