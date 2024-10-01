package com.adyen.testcards.main

import com.adyen.testcards.domain.CombinedPaymentMethods

sealed interface MainUIState {

    val isLoading: Boolean
    val query: String
    val errorMessages: List<String>

    data class Content(
        override val isLoading: Boolean,
        override val query: String,
        override val errorMessages: List<String>,
        val paymentMethods: CombinedPaymentMethods,
    ) : MainUIState

    data class Empty(
        override val isLoading: Boolean,
        override val query: String,
        override val errorMessages: List<String>,
    ) : MainUIState

    data class Error(
        override val isLoading: Boolean,
        override val query: String,
        override val errorMessages: List<String>,
    ) : MainUIState
}
