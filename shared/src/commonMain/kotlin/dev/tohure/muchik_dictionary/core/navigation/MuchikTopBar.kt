package dev.tohure.muchik_dictionary.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.ClayLight
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.OffWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MuchikTopBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OffWhite)
            .statusBarsPadding(),
    ) {
        DecorativeBar()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            if (maxWidth > 600.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    LogoSection(modifier = Modifier.width(240.dp))
                    Spacer(Modifier.width(24.dp))
                    NavTabsSection(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LogoSection()
                    Spacer(Modifier.height(12.dp))
                    NavTabsSection(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        HorizontalDivider(color = ClayLight, thickness = 1.dp)
    }
}

@Composable
private fun DecorativeBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(DarkClay),
    )
}

@Composable
private fun LogoSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "ENCICLOPEDIA MUCHIK",
            style = MaterialTheme.typography.headlineSmall,
            color = DarkClay,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = "Léxico, historia y revitalización de la lengua yunga",
            style = MaterialTheme.typography.bodySmall,
            color = Clay,
            fontStyle = FontStyle.Italic,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavTabsSection(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier = modifier) {
        Screen.navItems.forEach { screen ->
            NavTabItem(
                label = screen.label,
                isActive = currentRoute == screen.route,
                onClick = { onNavigate(screen) },
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isActive) DarkClay else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .padding(top = 3.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    color = if (isActive) DarkClay else Color.Transparent,
                    shape = RoundedCornerShape(1.dp),
                ),
        )
    }
}
