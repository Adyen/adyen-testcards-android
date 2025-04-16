package com.adyen.testcards.autofill

import android.app.PendingIntent
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.Field
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.Presentations
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.util.Log
import android.widget.RemoteViews
import com.adyen.testcards.R
import java.util.concurrent.atomic.AtomicInteger

class AdyenTestCardsAutofillService : AutofillService() {

    private val requestCode = AtomicInteger()

    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {
        Log.d(TAG, "onFillRequest")
        var pendingIntent: PendingIntent? = null

        cancellationSignal.setOnCancelListener {
            try {
                pendingIntent?.cancel()
            } catch (e: Exception) {
                Log.e(TAG, "Error while cancelling pending intent", e)
            }
        }

        val structure = request.fillContexts.last().structure
        val parsedStructure = StructureParser().parse(structure) ?: return

        val responseBuilder = FillResponse.Builder()

        pendingIntent = AutofillActivity.createPendingIntent(
            context = applicationContext,
            parsedStructure = parsedStructure,
            requestCode = requestCode.getAndIncrement(),
        )
        val remoteViews = RemoteViews(packageName, R.layout.item_autofill_entry)

        val datasetBuilder = Dataset.Builder()
        parsedStructure.allIds().forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val presentations = Presentations.Builder()
                    .setMenuPresentation(remoteViews)
                    .setDialogPresentation(remoteViews)
                    .build()
                datasetBuilder.setField(
                    it,
                    Field.Builder().setPresentations(presentations).build(),
                )
            } else {
                @Suppress("DEPRECATION")
                datasetBuilder.setValue(it, null, remoteViews)
            }
        }

        datasetBuilder.setAuthentication(pendingIntent.intentSender)

        responseBuilder.addDataset(datasetBuilder.build())

        callback.onSuccess(responseBuilder.build())
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        callback.onSuccess()
    }

    companion object {
        private const val TAG = "TestCardAutofillService"
    }
}
