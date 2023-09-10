package io.github.caimucheng.leaf.ide.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.ui.theme.LeafIDETheme
import io.github.caimucheng.leaf.common.util.launchMode
import io.github.caimucheng.leaf.common.util.setupMainActivity
import io.github.caimucheng.leaf.ide.LeafIDENavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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