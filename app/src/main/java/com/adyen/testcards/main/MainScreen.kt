package com.adyen.testcards.main

import android.content.Intent
import android.provider.Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE
import android.view.autofill.AutofillManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adyen.testcards.R
import com.adyen.testcards.ui.AdyenSearchBar
import com.adyen.testcards.ui.creditCardSection
import com.adyen.testcards.ui.favoritesSection
import com.adyen.testcards.ui.giftCardSection
import com.adyen.testcards.ui.ibanSection
import com.adyen.testcards.ui.upiSection
import com.adyen.testcards.ui.usernamePasswordSection

@Composable
internal fun MainScreen(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    mainViewModel: MainViewModel = viewModel(),
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onRefresh = mainViewModel::onRefresh,
        onErrorDismiss = mainViewModel::onErrorDismiss,
        onQueryChange = mainViewModel::onQueryChange,
        onFavoriteClick = mainViewModel::onFavoriteClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    uiState: MainUIState,
    snackBarHostState: SnackbarHostState,
    onRefresh: () -> Unit,
    onErrorDismiss: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (Any, Boolean) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                AdyenSearchBar(
                    query = uiState.query,
                    onQueryChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = remember(uiState) { uiState.isLoading },
            onRefresh = onRefresh,
        ) {
            when (uiState) {
                is MainUIState.Content -> MainContent(uiState, onFavoriteClick)
                is MainUIState.Empty -> MainEmpty()
                is MainUIState.Error -> MainError()
            }
        }
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages.first() }
        val onRefreshState by rememberUpdatedState(onRefresh)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(errorMessage, snackBarHostState) {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Retry",
                duration = SnackbarDuration.Short,
            )
            if (snackBarResult == SnackbarResult.ActionPerformed) {
                onRefreshState()
            }
            onErrorDismissState(errorMessage)
        }
    }

    val currentContext = LocalContext.current
    var shouldShowDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val autofillManager = currentContext.getSystemService(AutofillManager::class.java)
        shouldShowDialog = autofillManager.isAutofillSupported && !autofillManager.hasEnabledAutofillServices()
    }

    if (shouldShowDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog = false },
            icon = { Icon(Icons.Default.Settings, null) },
            title = { Text("Almost there!") },
            text = { Text("Enable the Adyen Test Cards service to easily fill in test data.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentContext.startActivity(
                            Intent(ACTION_REQUEST_SET_AUTOFILL_SERVICE, "package:com.adyen.testcards".toUri()),
                        )
                        shouldShowDialog = false
                    },
                ) { Text("Go to settings") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        shouldShowDialog = false
                    },
                ) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun MainContent(
    uiState: MainUIState.Content,
    onFavoriteClick: (Any, Boolean) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        val paymentMethods = uiState.paymentMethods

        if (paymentMethods.hasFavorites()) {
            favoritesSection(
                paymentMethods.favoriteCreditCards,
                paymentMethods.favoriteGiftCards,
                paymentMethods.favoriteIBANs,
                paymentMethods.favoriteUPIs,
                paymentMethods.favoriteUsernamePasswords,
                onFavoriteClick,
            )
        }

        if (paymentMethods.creditCards.isNotEmpty()) {
            creditCardSection(paymentMethods.creditCards, onFavoriteClick)
        }

        if (paymentMethods.giftCards.isNotEmpty()) {
            giftCardSection(paymentMethods.giftCards, onFavoriteClick)
        }

        if (paymentMethods.ibans.isNotEmpty()) {
            ibanSection(paymentMethods.ibans, onFavoriteClick)
        }

        if (paymentMethods.upis.isNotEmpty()) {
            upiSection(paymentMethods.upis, onFavoriteClick)
        }

        if (paymentMethods.usernamePasswords.isNotEmpty()) {
            usernamePasswordSection(paymentMethods.usernamePasswords, onFavoriteClick)
        }

        item {
            val context = LocalContext.current
            context.packageManager.getPackageInfo(context.packageName, 0).versionName?.let { version ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Version: $version", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun MainEmpty() {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                text = "Improve your search query or try pulling to refresh the content.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp, 8.dp),
            )
        }

        val context = LocalContext.current
        context.packageManager.getPackageInfo(context.packageName, 0).versionName?.let { version ->
            Text(
                "Version: $version",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun MainError() {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                text = "Try pulling to refresh the content.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp, 8.dp),
            )
        }

        val context = LocalContext.current
        context.packageManager.getPackageInfo(context.packageName, 0).versionName?.let { version ->
            Text(
                "Version: $version",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}
