package io.github.caimucheng.leaf.common.ui.preferences.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.caimucheng.leaf.common.model.Preference

@Composable
internal fun TextPreferenceWidget(
    preference: Preference.PreferenceItem.TextPreference,
    onClick: () -> Unit = {}
) {
    BasePreferenceWidget(preference = preference, onClick = onClick)
}