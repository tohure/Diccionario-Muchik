package dev.tohure.muchik_dictionary

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.tohure.muchik_dictionary.core.di.initKoin
import dev.tohure.muchik_dictionary.core.di.platformModule
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformModule)
    ComposeViewport(document.body!!) {
        App()
    }
}
