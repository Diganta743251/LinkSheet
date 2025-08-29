package fe.linksheet.composable.page.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.ContentType
import fe.composekit.component.list.item.ContentPosition
import fe.composekit.component.list.item.toEnabledContentSet
import fe.linksheet.R
import fe.linksheet.composable.component.list.item.type.PreferenceRadioButtonListItem
import fe.linksheet.composable.component.list.item.type.PreferenceSwitchListItem
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage
import fe.linksheet.module.viewmodel.ThemeSettingsViewModel
import fe.linksheet.composable.ui.ThemeV2
import fe.composekit.core.AndroidVersion
import fe.composekit.preference.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel


private val themes = arrayOf(ThemeV2.Light, ThemeV2.Dark, ThemeV2.System)

@Composable
fun ThemeSettingsRoute(onBackPressed: () -> Unit, viewModel: ThemeSettingsViewModel = koinViewModel()) {
    SaneScaffoldSettingsPage(headline = stringResource(id = R.string.theme), onBackPressed = onBackPressed) {
        if (AndroidVersion.isAtLeastApi31S()) {
            item(key = R.string.theme_enable_material_you, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.themeMaterialYou,
                    headlineContent = textContent(R.string.theme_enable_material_you),
                    supportingContent = textContent(R.string.theme_enable_material_you_explainer)
                )
            }

            divider(id =  R.string.theme_mode)
        }

        group(size = 4) {
            items(array = themes) { item, padding, shape ->
                PreferenceRadioButtonListItem(
                    shape = shape,
                    padding = padding,
                    value = item,
                    statePreference = viewModel.themeV2,
                    position = ContentPosition.Trailing,
                    headlineContent = textContent(item.id)
                )
            }

            item(key = R.string.theme_enable_amoled) { padding, shape ->
                val themeV2 by viewModel.themeV2.collectAsStateWithLifecycle()

                PreferenceSwitchListItem(
                    enabled = (themeV2 == ThemeV2.System || themeV2 == ThemeV2.Dark).toEnabledContentSet(),
                    shape = shape,
                    padding = padding,
                    statePreference = viewModel.themeAmoled,
                    headlineContent = textContent(R.string.theme_enable_amoled),
                    supportingContent = textContent(R.string.theme_enable_amoled_explainer)
                )
            }

            divider(id = R.string.theme_accent_color)

            item(key = R.string.theme_accent_color) { padding, shape ->
                PreferenceSwitchListItem(
                    shape = shape,
                    padding = padding,
                    statePreference = viewModel.themeAccentOverrideEnabled,
                    headlineContent = textContent(R.string.theme_accent_color),
                    supportingContent = textContent(R.string.theme_accent_color_explainer)
                )
            }

            item(key = R.string.theme_accent_palette) { padding, shape ->
                val palette = listOf(
                    0xFF6750A4, // Default seed
                    0xFF6D28D9,
                    0xFF2563EB,
                    0xFF059669,
                    0xFFEAB308,
                    0xFFEA580C,
                    0xFFDC2626
                )

                Row(
                    modifier = Modifier
                        .padding(padding)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (seed in palette) {
                        ColorSwatch(color = Color(seed), onClick = { viewModel.themeAccentSeed.update(seed.toInt()) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, onClick: () -> Unit) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.CircleShape)
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
            .then(Modifier.padding(end = 0.dp)),
        onDraw = {
            drawCircle(color = color, radius = size.minDimension / 2)
        }
    )
}
