package com.adyen.testcards.autofill

import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.autofill.HintConstants

internal class StructureParser {

    fun parse(structure: AssistStructure): ParsedStructure? {
        val result = ParsedStructure()

        for (i in 0 until structure.windowNodeCount) {
            val node = structure.getWindowNodeAt(i)
            val applicationId = node.title.split("/").firstOrNull().orEmpty()
            when {
                blockedAppIds.any { applicationId.startsWith(it) } -> {
                    Log.d(TAG, "Application ID ignored: $applicationId")
                }

                else -> {
                    result.applicationId = applicationId
                    parseNode(node.rootViewNode, result)
                }
            }
        }

        if (result.usernameId == null && result.passwordId != null) {
            result.usernameId = result.usernameCandidate
        }

        return if (result.isValid()) {
            result
        } else {
            null
        }
    }

    private fun parseNode(node: ViewNode, result: ParsedStructure) {
        if (node.visibility == View.VISIBLE) {
            if (node.autofillId != null) {
                if (!node.autofillHints.isNullOrEmpty()) {
                    parseNodeByAutofillHint(node, result)
                } else if (node.htmlInfo != null) {
                    parseNodeByHtmlInfo(node, result)
                } else {
                    parseNodeByInputType(node, result)
                }
            }

            for (i in 0 until node.childCount) {
                parseNode(node.getChildAt(i), result)
            }
        }
    }

    private fun parseNodeByAutofillHint(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by autofill hint")
        node.autofillHints?.forEach { hint ->
            when {
                hint.contains(View.AUTOFILL_HINT_USERNAME, true)
                    || hint.contains(View.AUTOFILL_HINT_EMAIL_ADDRESS, true)
                    || hint.contains(View.AUTOFILL_HINT_PHONE, true)
                    || hint.contains(AUTOFILL_HINT_EMAIL, true)
                    || hint.contains(AUTOFILL_HINT_LOGIN, true) -> {
                    if (result.passwordId == null) {
                        result.usernameId = node.autofillId
                    } else {
                        result.usernameCandidate = node.autofillId
                    }
                }

                hint.contains(View.AUTOFILL_HINT_PASSWORD, true) -> {
                    result.passwordId = node.autofillId
                }

                hint.contains(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, true) -> {
                    result.creditCardNumberId = node.autofillId
                }

                hint.contains(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE, true) -> {
                    result.creditCardExpiryDateId = node.autofillId
                }

                hint.contains(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE, true) -> {
                    result.creditCardSecurityCodeId = node.autofillId
                }

                hint.contains(HintConstants.AUTOFILL_HINT_GIFT_CARD_NUMBER, true) -> {
                    result.giftCardNumberId = node.autofillId
                }

                hint.contains(HintConstants.AUTOFILL_HINT_GIFT_CARD_PIN, true) -> {
                    result.giftCardPinId = node.autofillId
                }

                hint.contains(HintConstants.AUTOFILL_HINT_UPI_VPA, true) -> {
                    result.upiVpaId = node.autofillId
                }
            }
        }
    }

    private fun parseNodeByHtmlInfo(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by html info")
        val htmlInfo = node.htmlInfo
        if (htmlInfo?.tag?.lowercase() == "input") {
            htmlInfo.attributes?.forEach { attrPair ->
                if (attrPair.first.lowercase() == "type") {
                    when (attrPair.second.lowercase()) {
                        ATTR_TEL, ATTR_EMAIL -> {
                            if (result.passwordId == null) {
                                result.usernameId = node.autofillId
                            }
                        }

                        ATTR_TEXT -> {
                            if (result.passwordId == null) {
                                result.usernameCandidate = node.autofillId
                            }
                        }

                        ATTR_PASSWORD -> {
                            result.passwordId = node.autofillId
                        }
                    }
                }
            }
        }
    }

    private fun parseNodeByInputType(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by input type")
        when (node.inputType and InputType.TYPE_MASK_CLASS) {
            InputType.TYPE_CLASS_TEXT -> parseByInputTypeText(node, result)
            InputType.TYPE_CLASS_NUMBER -> parseByInputTypeNumber(node, result)
            InputType.TYPE_NULL -> parseByInputTypeNull(node, result)
        }
    }

    private fun parseByInputTypeText(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by input type - text")
        when (node.inputType and InputType.TYPE_MASK_VARIATION) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS -> {
                if (result.passwordId == null) {
                    result.usernameId = node.autofillId
                }
            }

            InputType.TYPE_TEXT_VARIATION_NORMAL,
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
            InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT -> {
                if (result.passwordId == null) {
                    result.usernameCandidate = node.autofillId
                }
            }

            InputType.TYPE_TEXT_VARIATION_PASSWORD,
            InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD -> {
                result.passwordId = node.autofillId
            }
        }
    }

    private fun parseByInputTypeNumber(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by input type - number")
        when (node.inputType and InputType.TYPE_MASK_VARIATION) {
            InputType.TYPE_NUMBER_VARIATION_NORMAL -> {
                if (result.usernameCandidate == null) {
                    result.usernameCandidate = node.autofillId
                }
            }

            InputType.TYPE_NUMBER_VARIATION_PASSWORD -> {
                result.passwordId = node.autofillId
            }
        }
    }

    private fun parseByInputTypeNull(node: ViewNode, result: ParsedStructure) {
        Log.d(TAG, "Parsing by input type - null")
        when (node.className) {
            EDIT_TEXT_CLASSNAME,
            APP_COMPAT_EDIT_TEXT_CLASSNAME -> {
                if (result.passwordId == null) {
                    result.usernameCandidate = node.autofillId
                }
            }
        }
    }

    companion object {
        private const val TAG = "StructureParser"

        private const val AUTOFILL_HINT_EMAIL = "email"
        private const val AUTOFILL_HINT_LOGIN = "login"

        private const val ATTR_EMAIL = "email"
        private const val ATTR_PASSWORD = "password"
        private const val ATTR_TEL = "tel"
        private const val ATTR_TEXT = "text"

        private const val EDIT_TEXT_CLASSNAME = "android.widget.EditText"
        private const val APP_COMPAT_EDIT_TEXT_CLASSNAME = "androidx.appcompat.widget.AppCompatEditText"

        private val blockedAppIds = listOf(
            "PopupWindow:",
            "com.android.settings",
            "com.google.android.settings",
        )
    }
}
