package com.adyen.testcards.autofill

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adyen.testcards.R
import com.adyen.testcards.ui.AdyenSearchBar
import com.adyen.testcards.ui.creditCardSection
import com.adyen.testcards.ui.favoritesSection
import com.adyen.testcards.ui.giftCardSection
import com.adyen.testcards.ui.ibanSection
import com.adyen.testcards.ui.upiSection
import com.adyen.testcards.ui.usernamePasswordSection

@Composable
internal fun AutofillScreen(
    viewModel: AutofillViewModel,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AutofillScreen(
        uiState,
        onDismiss,
        viewModel::onQueryChange,
        viewModel::onFavoriteClick,
        viewModel::onPaymentMethodClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutofillScreen(
    uiState: AutofillUIState,
    onDismiss: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (Any, Boolean) -> Unit,
    onPaymentMethodClick: (Any) -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Box {
            AdyenSearchBar(
                query = uiState.query,
                onQueryChange = onQueryChange,
                modifier = Modifier.align(Alignment.TopCenter),
            )

            when (uiState) {
                is AutofillUIState.Content -> AutofillContent(uiState, onFavoriteClick, onPaymentMethodClick)
                is AutofillUIState.Empty -> if (!uiState.isLoading) AutofillEmpty()
                is AutofillUIState.Error -> AutofillError()
            }
        }
    }
}

@Composable
private fun AutofillContent(
    uiState: AutofillUIState.Content,
    onFavoriteClick: (Any, Boolean) -> Unit,
    onPaymentMethodClick: (Any) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp, 72.dp, 0.dp, 0.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        val paymentMethods = uiState.paymentMethods

        if (paymentMethods.hasFavorites()) {
            favoritesSection(
                paymentMethods.favoriteCreditCards,
                paymentMethods.favoriteGiftCards,
                paymentMethods.favoriteIBANs,
                paymentMethods.favoriteUPIs,
                paymentMethods.favoriteUsernamePasswords,
                onFavoriteClick,
                onPaymentMethodClick,
            )
        }

        if (paymentMethods.creditCards.isNotEmpty()) {
            creditCardSection(paymentMethods.creditCards, onFavoriteClick, onPaymentMethodClick)
        }

        if (paymentMethods.giftCards.isNotEmpty()) {
            giftCardSection(paymentMethods.giftCards, onFavoriteClick, onPaymentMethodClick)
        }

        if (paymentMethods.ibans.isNotEmpty()) {
            ibanSection(paymentMethods.ibans, onFavoriteClick, onPaymentMethodClick)
        }

        if (paymentMethods.upis.isNotEmpty()) {
            upiSection(paymentMethods.upis, onFavoriteClick, onPaymentMethodClick)
        }

        if (paymentMethods.usernamePasswords.isNotEmpty()) {
            usernamePasswordSection(paymentMethods.usernamePasswords, onFavoriteClick, onPaymentMethodClick)
        }
    }
}

@Composable
private fun AutofillEmpty() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.card_phone_payment),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(.8f),
        )
        Spacer(Modifier.padding(32.dp))
        Text("No content!", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Try improving your search query.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp, 8.dp),
        )
    }
}

@Composable
private fun AutofillError() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.man_shrugging),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(.8f),
        )
        Spacer(Modifier.padding(32.dp))
        Text("Error!", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Whoops, something went wrong...",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp, 8.dp),
        )
    }
}
