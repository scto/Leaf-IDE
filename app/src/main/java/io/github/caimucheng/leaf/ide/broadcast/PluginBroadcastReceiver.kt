package io.github.caimucheng.leaf.ide.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.caimucheng.leaf.ide.application.appViewModel
import io.github.caimucheng.leaf.ide.viewmodel.AppUIIntent

class PluginBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extraReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
        when {
            !extraReplacing && intent.action == Intent.ACTION_PACKAGE_ADDED -> {
                Log.e("Broadcast", "App added")
                appViewModel.intent.trySend(AppUIIntent.Refresh)
            }

            !extraReplacing && intent.action == Intent.ACTION_PACKAGE_REMOVED -> {
                Log.e("Broadcast", "App removed")
                appViewModel.intent.trySend(AppUIIntent.Refresh)
            }

            intent.action == Intent.ACTION_PACKAGE_REPLACED -> {
                Log.e("Broadcast", "App replaced")
                appViewModel.intent.trySend(AppUIIntent.Refresh)
            }
        }
    }

}