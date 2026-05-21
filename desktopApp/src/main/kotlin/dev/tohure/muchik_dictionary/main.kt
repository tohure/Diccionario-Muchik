package dev.tohure.muchik_dictionary

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.tohure.muchik_dictionary.core.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Enciclopedia Muchik",
        ) {
            App()
        }
    }
}