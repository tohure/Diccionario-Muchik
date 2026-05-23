package dev.tohure.muchik_dictionary

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.tohure.muchik_dictionary.core.di.initKoin
import dev.tohure.muchik_dictionary.core.di.platformModule

fun main() {
    initKoin(platformModule)
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Enciclopedia Muchik",
        ) {
            App()
        }
    }
}