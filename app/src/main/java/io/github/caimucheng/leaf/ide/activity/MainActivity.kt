package io.github.caimucheng.leaf.ide.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.ui.theme.LeafIDETheme
import io.github.caimucheng.leaf.common.util.LAUNCH_MODE
import io.github.caimucheng.leaf.common.util.isExternalLaunchMode
import io.github.caimucheng.leaf.common.util.launchMode
import io.github.caimucheng.leaf.common.util.setupMainActivity
import io.github.caimucheng.leaf.common.util.sharedPreferences
import io.github.caimucheng.leaf.ide.navhost.LeafIDENavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launchModeValue = sharedPreferences.getString(LAUNCH_MODE, null)
        if (launchModeValue != null) {
            if (isExternalLaunchMode) {
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                val readPermissionKind = ContextCompat.checkSelfPermission(this, permissions[0])
                val writePermissionKind = ContextCompat.checkSelfPermission(this, permissions[1])
                if (readPermissionKind == PackageManager.PERMISSION_DENIED || writePermissionKind == PackageManager.PERMISSION_DENIED) {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                    return
                }
            }
        }

        setupMainActivity(launchMode())

        setContent {
            LeafIDETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pageNavController = rememberNavController()
                    LeafIDENavHost(
                        pageNavController = pageNavController
                    )
                }
            }
        }
    }
}