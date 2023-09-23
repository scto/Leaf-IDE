package io.github.caimucheng.leaf.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(onDismissRequest: () -> Unit = {}, title: String? = null) {
    if (!title.isNullOrEmpty()) {
        AlertDialog(onDismissRequest = onDismissRequest) {
            Card(
                shape = AlertDialogDefaults.shape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(Modifier.padding(30.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = title,
                        fontSize = 18.sp
                    )
                }
            }
        }
    } else {
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                shape = AlertDialogDefaults.shape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(Modifier.padding(30.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                    )
                }
            }
        }
    }
}