package io.github.caimucheng.leaf.common.ui.preferences

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.caimucheng.leaf.common.manager.DataStoreManager
import io.github.caimucheng.leaf.common.model.Preference
import io.github.caimucheng.leaf.common.ui.preferences.widget.PreferenceGroupHeaderWidget

@Composable
fun PreferenceScreen(
    items: List<Preference>,
    modifier: Modifier = Modifier,
    dataStore: DataStore<Preferences>,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val dataStoreManager = remember {
        DataStoreManager(dataStore)
    }
    val prefs by dataStoreManager.preferenceFlow.collectAsState(initial = null)

    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        for ((index, preference) in items.withIndex()) {
            when (preference) {
                // Create Preference Group
                is Preference.PreferenceGroup -> {
                    item {
                        val lastIsPreferenceGroup =
                            index - 1 >= 0 && items[index - 1] is Preference.PreferenceGroup
                        if (lastIsPreferenceGroup) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp),
                                color = DividerDefaults.color.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        PreferenceGroupHeaderWidget(title = preference.title)
                    }
                    items(preference.preferenceItems) { item ->
                        PreferenceItem(
                            item = item,
                            prefs = prefs,
                            dataStoreManager = dataStoreManager
                        )
                    }
                }

                // Create Preference Item
                is Preference.PreferenceItem<*> -> item {
                    PreferenceItem(
                        item = preference,
                        prefs = prefs,
                        dataStoreManager = dataStoreManager
                    )
                }
            }
        }
    }
}