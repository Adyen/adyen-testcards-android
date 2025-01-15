package com.adyen.testcards.data

import com.adyen.testcards.R

internal object LogoMapping {

    fun getResourceId(type: String): Int? = when (type) {
        "amex" -> R.drawable.ic_pm_amex
        "bank" -> R.drawable.ic_pm_bank
        "cartebancaire" -> R.drawable.ic_pm_carte_bancaire
        "cup" -> R.drawable.ic_pm_unionpay
        "dankort" -> R.drawable.ic_pm_dankort
        "diners" -> R.drawable.ic_pm_diners
        "discover" -> R.drawable.ic_pm_discover
        "jcb" -> R.drawable.ic_pm_jcb
        "maestro" -> R.drawable.ic_pm_maestro
        "mc" -> R.drawable.ic_pm_mastercard
        "visa" -> R.drawable.ic_pm_visa
        "vpay" -> R.drawable.ic_pm_vpay
        else -> null
    }
}
