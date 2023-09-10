package io.github.caimucheng.leaf.common.ui.preferences.widget

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import io.github.caimucheng.leaf.common.model.Preference

@Composable
internal fun SwitchPreferenceWidget(
    preference: Preference.PreferenceItem.SwitchPreference,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    BasePreferenceWidget(preference = preference, onClick = {
        onValueChange(!value)
    }) {
        Switch(checked = value, onCheckedChange = null)
    }
}