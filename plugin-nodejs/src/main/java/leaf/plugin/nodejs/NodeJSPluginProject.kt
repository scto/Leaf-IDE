package leaf.plugin.nodejs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import io.github.caimucheng.leaf.plugin.PluginProject

class NodeJSPluginProject : PluginProject() {

    override fun getDisplayedPictureResId(): Int {
        return R.mipmap.displayed_nodejs_picture
    }

    override fun getDisplayedTitleId(): Int {
        return R.string.displayed_nodejs_title
    }

    override fun getDisplayedProjectLogoResId(): Int {
        return R.drawable.nodejs_logo
    }

    override fun getDisplayedProjectTitleId(): Int {
        return R.string.displayed_project_title
    }

    @Composable
    override fun CreateProjectDialog(onDismissRequest: () -> Unit, onNavigateHome: () -> Unit) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(text = stringResource(id = R.string.displayed_nodejs_title))
            },
            confirmButton = {
                TextButton(onClick = {

                }) {
                    Text(text = stringResource(id = R.string.create))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissRequest()
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

}