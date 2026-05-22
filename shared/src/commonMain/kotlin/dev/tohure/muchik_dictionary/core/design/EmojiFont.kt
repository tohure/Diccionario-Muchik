package dev.tohure.muchik_dictionary.core.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

// wasmJs carga NotoEmoji explícitamente (los browsers no tienen fuente emoji del sistema).
// Android, iOS y Desktop usan FontFamily.Default porque sus plataformas la proveen de forma nativa.
@Composable
expect fun rememberEmojiFont(): FontFamily
