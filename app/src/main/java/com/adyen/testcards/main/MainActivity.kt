package com.adyen.testcards.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adyen.testcards.ui.theme.AdyenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdyenTheme {
                MainScreen()
            }
        }
    }
}
