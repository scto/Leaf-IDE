package io.github.caimucheng.leaf.ide.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.caimucheng.leaf.common.component.LeafApp
import io.github.caimucheng.leaf.common.ui.theme.LeafIDETheme
import io.github.caimucheng.leaf.ide.R
import kotlinx.coroutines.launch

class CrashHandlerActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deviceInfo = intent.getStringExtra("deviceInfo") ?: "null"
        val threadGroup = intent.getStringExtra("threadGroup") ?: "null"
        val thread = intent.getStringExtra("thread") ?: "null"
        val exception = intent.getStringExtra("exception") ?: "null"

        setContent {
            LeafIDETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val all = buildString {
                        appendLine(stringResource(id = R.string.app_crash_header))
                        appendLine()
                        appendLine(stringResource(id = R.string.device_info))
                        appendLine(deviceInfo)
                        appendLine()
                        appendLine(
                            stringResource(
                                id = R.string.thread_group,
                                threadGroup
                            )
                        )
                        appendLine(stringResource(id = R.string.thread, thread))
                        append(
                            stringResource(
                                id = R.string.exception,
                                exception
                            )
                        )
                    }
                    val snackbarHostState = remember { SnackbarHostState() }
                    val coroutineScope = rememberCoroutineScope()
                    LeafApp(
                        title = stringResource(id = R.string.app_crashed),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        content = {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(it)
                            ) {
                                val beNotAllowedBack =
                                    stringResource(id = R.string.be_not_allowed_back)
                                val callback = remember {
                                    object : OnBackPressedCallback(true) {

                                        override fun handleOnBackPressed() {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    beNotAllowedBack,
                                                    withDismissAction = true
                                                )
                                            }
                                        }

                                    }
                                }
                                val dispatcher =
                                    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                                SelectionContainer {
                                    Text(
                                        text = all,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .verticalScroll(rememberScrollState())
                                    )
                                }
                                DisposableEffect(key1 = Unit, effect = {
                                    dispatcher?.addCallback(callback)
                                    onDispose {
                                        callback.remove()
                                    }
                                })
                            }
                        },
                        bottomBar = {
                            BottomAppBar {
                                val copiedSuccessfully =
                                    stringResource(id = R.string.copied_successfully)
                                val copiedFailed = stringResource(id = R.string.copied_failed)
                                FilledTonalIconButton(
                                    onClick = {
                                        try {
                                            copyText(all)
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    copiedSuccessfully,
                                                    withDismissAction = true
                                                )
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    copiedFailed + e.message,
                                                    withDismissAction = true
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(start = 20.dp)
                                        .fillMaxHeight()
                                        .padding(top = 15.dp, bottom = 15.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                FilledIconButton(
                                    onClick = {
                                        restartApp()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(end = 20.dp)
                                        .fillMaxHeight()
                                        .padding(top = 15.dp, bottom = 15.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.RestartAlt,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun copyText(all: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("exception", all))
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        android.os.Process.killProcess(android.os.Process.myPid())
    }

}