package com.abaiaor.ultrasavenothing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity hosting the Compose navigation graph. No business logic, no direct
 * ViewModel/UseCase/Repository access — see docs/architecture.md.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UltraSaveNothingRoot()
        }
    }
}
