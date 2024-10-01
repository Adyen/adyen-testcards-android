package com.adyen.testcards.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.testcards.domain.CombinedPaymentMethods
import com.adyen.testcards.domain.FavoriteItemUseCase
import com.adyen.testcards.domain.GetPaymentMethodsUseCase
import com.adyen.testcards.domain.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val favoriteItemUseCase: FavoriteItemUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        MainViewModelState(
            isLoading = true,
        ),
    )

    private val searchResults: MutableStateFlow<CombinedPaymentMethods?> = MutableStateFlow(null)

    private val paymentMethodFlow = getPaymentMethodsUseCase.getPaymentMethods()
        .onEach { setLoadingState(false) }

    val uiState: StateFlow<MainUIState> = combine(
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
                    val errorMessage = "Failed to fetch payment methods."
                    Log.e("MainViewModel", errorMessage, it)
                    viewModelState.copy(errorMessages = listOf(errorMessage))
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

    private fun setLoadingState(isLoading: Boolean) {
        viewModelState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun onRefresh() {
        setLoadingState(true)
        viewModelScope.launch {
            getPaymentMethodsUseCase.refresh()
        }
    }

    fun onErrorDismiss(errorMessage: String) {
        viewModelState.update { currentState ->
            val newErrorMessages = currentState.errorMessages.filterNot { it == errorMessage }
            currentState.copy(errorMessages = newErrorMessages)
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
}

private data class MainViewModelState(
    val isLoading: Boolean = false,
    val query: String = "",
    val errorMessages: List<String> = emptyList(),
    val paymentMethods: CombinedPaymentMethods = CombinedPaymentMethods(),
) {

    fun toUIState() = when {
        errorMessages.isNotEmpty() && paymentMethods.isEmpty() -> MainUIState.Error(
            isLoading = isLoading,
            query = query,
            errorMessages = errorMessages,
        )

        paymentMethods.isEmpty() -> MainUIState.Empty(
            isLoading = isLoading,
            query = query,
            errorMessages = errorMessages,
        )

        else -> MainUIState.Content(
            isLoading = isLoading,
            query = query,
            errorMessages = errorMessages,
            paymentMethods = paymentMethods,
        )
    }
}
