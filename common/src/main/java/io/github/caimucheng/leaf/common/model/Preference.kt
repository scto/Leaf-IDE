package io.github.caimucheng.leaf.common.model

import androidx.compose.runtime.Composable

sealed class Preference {

    abstract val title: String
    abstract val enabled: Boolean
    abstract val visible: Boolean

    /**
     * A single [Preference] item
     */
    sealed class PreferenceItem<T> : Preference() {
        abstract val summary: String
        abstract val singleLineTitle: Boolean
        abstract val icon: (@Composable () -> Unit)?
        override val visible: Boolean = true

        /**
         * 	A basic [PreferenceItem] that only displays text.
         */
        data class TextPreference(
            override val title: String,
            override val summary: String,
            override val singleLineTitle: Boolean = true,
            override val icon: (@Composable () -> Unit) ?= null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val onClick: () -> Unit = {},
        ) : PreferenceItem<String>()

        /**
         * 	A [PreferenceItem] that provides a two-state toggleable option.
         */
        data class SwitchPreference(
            val request: PreferenceRequest<Boolean>,
            override val title: String,
            override val summary: String,
            override val singleLineTitle: Boolean = true,
            override val icon: (@Composable () -> Unit) ?= null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

        ) : PreferenceItem<Boolean>()

    }

    /**
     * A container for multiple [PreferenceItem]s
     */
    data class PreferenceGroup(
        override val title: String,
        override val enabled: Boolean = true,
        override val visible: Boolean = true,

        val preferenceItems: List<PreferenceItem<out Any>>
    ) : Preference()

}