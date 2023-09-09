package io.github.caimucheng.leaf.plugin.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.caimucheng.leaf.plugin.application.appViewModel
import io.github.caimucheng.leaf.plugin.viewmodel.AppUIIntent

class PluginBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED, Intent.ACTION_PACKAGE_REPLACED, Intent.ACTION_PACKAGE_REMOVED -> {
                Log.e("Broadcast", "App added/updated/removed")
                appViewModel.intent.trySend(AppUIIntent.Refresh)
            }
        }
    }

}