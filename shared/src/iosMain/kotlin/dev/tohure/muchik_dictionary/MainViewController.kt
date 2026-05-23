package dev.tohure.muchik_dictionary

import androidx.compose.ui.window.ComposeUIViewController
import dev.tohure.muchik_dictionary.core.di.initKoin
import dev.tohure.muchik_dictionary.core.di.platformModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin(platformModule)
    return ComposeUIViewController { App() }
}