package com.adyen.testcards.autofill

import com.adyen.testcards.domain.CombinedPaymentMethods

sealed interface AutofillUIState {

    val isLoading: Boolean
    val query: String

    data class Content(
        override val isLoading: Boolean,
        override val query: String,
        val paymentMethods: CombinedPaymentMethods,
    ) : AutofillUIState

    data class Empty(
        override val isLoading: Boolean,
        override val query: String,
    ) : AutofillUIState

    data class Error(
        override val isLoading: Boolean,
        override val query: String,
    ) : AutofillUIState
}
