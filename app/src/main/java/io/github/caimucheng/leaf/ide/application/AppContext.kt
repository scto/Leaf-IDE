package io.github.caimucheng.leaf.ide.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.github.caimucheng.leaf.ide.broadcast.PluginBroadcastReceiver
import io.github.caimucheng.leaf.ide.viewmodel.AppViewModel

class AppContext : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        lateinit var receiver: PluginBroadcastReceiver
            private set

        lateinit var appViewModel: AppViewModel
            private set

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        receiver = PluginBroadcastReceiver()
        appViewModel = AppViewModel(this)

        // Register the broadcast
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")
        registerReceiver(receiver, intentFilter)
    }

    override fun onTerminate() {
        // Unregister the broadcast
        unregisterReceiver(receiver)
        super.onTerminate()
    }

}

inline val appViewModel: AppViewModel
    get() {
        return AppContext.appViewModel
    }