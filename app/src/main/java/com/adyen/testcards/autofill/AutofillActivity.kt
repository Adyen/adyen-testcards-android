package com.adyen.testcards.autofill

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.autofill.AutofillManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.adyen.testcards.ui.theme.AdyenTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class AutofillActivity : ComponentActivity() {

    private val viewModel: AutofillViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.dataset
            .flowWithLifecycle(lifecycle)
            .onEach(::onDataset)
            .launchIn(lifecycleScope)

        enableEdgeToEdge()
        setContent {
            AdyenTheme {
                AutofillScreen(
                    viewModel = viewModel,
                    onDismiss = ::finish,
                )
            }
        }
    }

    private fun onDataset(dataset: Dataset) {
        val resultIntent = Intent().apply {
            putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, dataset)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    companion object {

        private const val EXTRA_PARSED_RESULT = "EXTRA_PARSED_RESULT"

        internal fun createPendingIntent(
            context: Context,
            parsedStructure: ParsedStructure,
            requestCode: Int,
        ): PendingIntent {
            val intent = Intent(context, AutofillActivity::class.java).apply {
                putExtra(EXTRA_PARSED_RESULT, parsedStructure)
            }

            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            } else {
                PendingIntent.FLAG_CANCEL_CURRENT
            }

            return PendingIntent.getActivity(context, requestCode, intent, flags)
        }
    }
}
