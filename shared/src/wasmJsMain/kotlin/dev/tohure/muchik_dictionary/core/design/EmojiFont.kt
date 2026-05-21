package dev.tohure.muchik_dictionary.core.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import dictionarymuchik.shared.generated.resources.NotoEmoji_Regular
import dictionarymuchik.shared.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
actual fun rememberEmojiFont(): FontFamily = FontFamily(Font(Res.font.NotoEmoji_Regular))
