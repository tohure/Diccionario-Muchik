package dev.tohure.muchik_dictionary.core.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.OffWhite
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.app_title
import dictionarymuchik.shared.generated.resources.menu_content_description
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuchikTopBar(
    isWideScreen: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.app_title),
                style = MaterialTheme.typography.titleLarge,
                color = DarkClay,
                fontWeight = FontWeight.ExtraBold,
            )
        },
        navigationIcon = {
            if (!isWideScreen) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = MenuIcon,
                        contentDescription = stringResource(Res.string.menu_content_description),
                        tint = DarkClay
                    )
                }
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = OffWhite,
                titleContentColor = DarkClay,
            ),
    )
}

private val MenuIcon =
    ImageVector
        .Builder(
            name = "Menu",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(3f, 18f)
            horizontalLineTo(21f)
            verticalLineTo(16f)
            horizontalLineTo(3f)
            verticalLineTo(18f)
            close()
            moveTo(3f, 13f)
            horizontalLineTo(21f)
            verticalLineTo(11f)
            horizontalLineTo(3f)
            verticalLineTo(13f)
            close()
            moveTo(3f, 6f)
            verticalLineTo(8f)
            horizontalLineTo(21f)
            verticalLineTo(6f)
            horizontalLineTo(3f)
            close()
        }.build()
