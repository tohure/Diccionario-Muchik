package dev.tohure.muchik_dictionary.core.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import dictionarymuchik.shared.generated.resources.NotoEmoji_Regular
import dictionarymuchik.shared.generated.resources.Res
import org.jetbrains.compose.resources.Font

// Los browsers no proveen fuente emoji del sistema; se carga explícitamente desde resources.
@Composable
actual fun rememberEmojiFont(): FontFamily = FontFamily(Font(Res.font.NotoEmoji_Regular))
