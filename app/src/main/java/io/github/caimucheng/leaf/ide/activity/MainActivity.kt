package io.github.caimucheng.leaf.ide.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.caimucheng.leaf.common.component.AnimatedNavHost
import io.github.caimucheng.leaf.common.ui.theme.LeafIDETheme
import io.github.caimucheng.leaf.ide.ui.CreateProjectPage
import io.github.caimucheng.leaf.ide.ui.MainPage

const val MAIN_PAGE = "/main_page"
const val CREATE_PROJECT_PAGE = "/create_project_page"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LeafIDETheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pageNavController = rememberNavController()
                    AnimatedNavHost(pageNavController, MAIN_PAGE) {
                        composable(MAIN_PAGE) {
                            MainPage(pageNavController)
                        }
                        composable(CREATE_PROJECT_PAGE) {
                            CreateProjectPage(pageNavController)
                        }
                    }
                }
            }
        }
    }

}