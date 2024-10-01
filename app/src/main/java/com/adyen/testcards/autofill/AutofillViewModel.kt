package com.adyen.testcards.autofill

import android.service.autofill.Dataset
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.testcards.domain.CombinedPaymentMethods
import com.adyen.testcards.domain.CreateDatasetUseCase
import com.adyen.testcards.domain.FavoriteItemUseCase
import com.adyen.testcards.domain.GetPaymentMethodsUseCase
import com.adyen.testcards.domain.PaymentMethodType
import com.adyen.testcards.domain.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AutofillViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchUseCase: SearchUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val favoriteItemUseCase: FavoriteItemUseCase,
    private val createDatasetUseCase: CreateDatasetUseCase,
) : ViewModel() {

    private val parsedStructure: ParsedStructure = savedStateHandle["EXTRA_PARSED_RESULT"]!!
    private val detectedPaymentMethod = detectPaymentMethod()

    private val _dataset = MutableSharedFlow<Dataset>()
    val dataset = _dataset.asSharedFlow()

    private val viewModelState = MutableStateFlow(
        AutofillViewModelState(
            isLoading = true,
        ),
    )

    private val searchResults: MutableStateFlow<CombinedPaymentMethods?> = MutableStateFlow(null)

    private val paymentMethodFlow = getPaymentMethodsUseCase.getPaymentMethods()
        // Filter payment methods to only show the detected payment method
        .map { result ->
            result.map { combinedPaymentMethods ->
                when (detectedPaymentMethod) {
                    PaymentMethodType.CREDIT_CARD -> CombinedPaymentMethods(
                        favoriteCreditCards = combinedPaymentMethods.favoriteCreditCards,
                        creditCards = combinedPaymentMethods.creditCards,
                    )

                    PaymentMethodType.GIFT_CARD -> CombinedPaymentMethods(
                        favoriteGiftCards = combinedPaymentMethods.favoriteGiftCards,
                        giftCards = combinedPaymentMethods.giftCards,
                    )

                    PaymentMethodType.UPI -> CombinedPaymentMethods(
                        favoriteUPIs = combinedPaymentMethods.favoriteUPIs,
                        upis = combinedPaymentMethods.upis,
                    )

                    PaymentMethodType.USERNAME_PASSWORD -> CombinedPaymentMethods(
                        favoriteUsernamePasswords = combinedPaymentMethods.favoriteUsernamePasswords,
                        usernamePasswords = combinedPaymentMethods.usernamePasswords,
                    )

                    PaymentMethodType.UNKNOWN -> combinedPaymentMethods
                }
            }
        }
        .onEach { viewModelState.update { it.copy(isLoading = false) } }

    val uiState: StateFlow<AutofillUIState> = combine(
        viewModelState,
        paymentMethodFlow,
        searchResults,
    ) { viewModelState, paymentMethods, searchResults ->
        if (searchResults != null) {
            viewModelState.copy(paymentMethods = searchResults)
        } else {
            paymentMethods.fold(
                onSuccess = {
                    backupPaymentMethods = it
                    viewModelState.copy(paymentMethods = it)
                },
                onFailure = {
                    Log.e("MainViewModel", "Failed to fetch payment methods.", it)
                    viewModelState.copy(hasError = true)
                },
            )
        }
    }
        .map { it.toUIState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUIState())

    private var backupPaymentMethods = viewModelState.value.paymentMethods

    init {
        viewModelScope.launch {
            getPaymentMethodsUseCase.refresh()
        }
    }

    fun onQueryChange(query: String) {
        viewModelState.update { it.copy(query = query) }
        viewModelScope.launch {
            searchResults.update {
                if (query.isNotBlank()) {
                    CombinedPaymentMethods(
                        favoriteCreditCards = emptyList(),
                        favoriteGiftCards = emptyList(),
                        favoriteIBANs = emptyList(),
                        favoriteUPIs = emptyList(),
                        favoriteUsernamePasswords = emptyList(),
                        creditCards = searchUseCase.searchCreditCards(query, backupPaymentMethods.creditCards),
                        giftCards = searchUseCase.searchGiftCards(query, backupPaymentMethods.giftCards),
                        ibans = searchUseCase.searchIBANs(query, backupPaymentMethods.ibans),
                        upis = searchUseCase.searchUPIs(query, backupPaymentMethods.upis),
                        usernamePasswords = searchUseCase.searchUsernamePasswords(
                            query,
                            backupPaymentMethods.usernamePasswords,
                        ),
                    )
                } else {
                    null
                }
            }
        }
    }

    fun onFavoriteClick(item: Any, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoriteItemUseCase.favorite(item)
            } else {
                favoriteItemUseCase.unfavorite(item)
            }
        }
    }

    fun onPaymentMethodClick(item: Any) {
        val dataset = createDatasetUseCase.createDataset(item, parsedStructure)
        viewModelScope.launch {
            _dataset.emit(dataset)
        }
    }

    private fun detectPaymentMethod(): PaymentMethodType {
        return with(parsedStructure) {
            when {
                creditCardNumberId != null ||
                    creditCardExpiryDateId != null ||
                    creditCardSecurityCodeId != null -> PaymentMethodType.CREDIT_CARD

                giftCardNumberId != null ||
                    giftCardPinId != null -> PaymentMethodType.GIFT_CARD

                upiVpaId != null -> PaymentMethodType.UPI

                usernameId != null ||
                    passwordId != null -> PaymentMethodType.USERNAME_PASSWORD

                else -> PaymentMethodType.UNKNOWN
            }
        }
    }
}

private data class AutofillViewModelState(
    val isLoading: Boolean = false,
    val query: String = "",
    val paymentMethods: CombinedPaymentMethods = CombinedPaymentMethods(),
    val hasError: Boolean = false,
) {

    fun toUIState() = when {
        hasError -> AutofillUIState.Error(
            isLoading = isLoading,
            query = query,
        )

        paymentMethods.isEmpty() -> AutofillUIState.Empty(
            isLoading = isLoading,
            query = query,
        )

        else -> AutofillUIState.Content(
            isLoading = isLoading,
            query = query,
            paymentMethods = paymentMethods,
        )
    }
}
