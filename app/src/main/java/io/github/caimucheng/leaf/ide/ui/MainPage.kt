package io.github.caimucheng.leaf.ide.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.caimucheng.leaf.common.component.NoImplementation
import io.github.caimucheng.leaf.ide.R

@Composable
fun Home() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (floatingActionButton) = createRefs()
        FloatingActionButton(
            onClick = { },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(48.dp),
            modifier = Modifier.constrainAs(floatingActionButton) {
                end.linkTo(parent.end, 15.dp)
                bottom.linkTo(parent.bottom, 15.dp)
            }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_project)
            )
        }
    }
}

@Composable
fun Plugin() {
    NoImplementation()
}

@Composable
fun LeafFlow() {
    NoImplementation()
}

@Composable
fun Settings() {
    NoImplementation()
}