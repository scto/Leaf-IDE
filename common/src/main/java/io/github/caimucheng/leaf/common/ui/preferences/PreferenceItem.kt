package io.github.caimucheng.leaf.common.ui.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.Preference
import io.github.caimucheng.leaf.common.ui.preferences.widget.SwitchPreferenceWidget
import io.github.caimucheng.leaf.common.ui.preferences.widget.TextPreferenceWidget
import kotlinx.coroutines.launch

@Composable
internal fun PreferenceItem(
    item: Preference.PreferenceItem<*>,
    prefs: Preferences?,
    dataStoreManager: DataStoreManager
) {
    val scope = rememberCoroutineScope()

    when (item) {
        is Preference.PreferenceItem.SwitchPreference -> {
            SwitchPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch { dataStoreManager.editPreference(item.request.key, newValue) }
                }
            )
        }

        is Preference.PreferenceItem.TextPreference -> {
            TextPreferenceWidget(
                preference = item,
                onClick = item.onClick
            )
        }
    }
}