package io.github.caimucheng.leaf.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun FileTab(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            ConstraintLayout {
                val (text, spacer) = createRefs()
                Text(text = "Text", modifier = Modifier.constrainAs(text) {
                    linkTo(parent.start, parent.end, startMargin = 5.dp, endMargin = 5.dp)
                    top.linkTo(parent.top)
                })
                Spacer(
                    modifier = Modifier
                        .constrainAs(spacer) {
                            linkTo(parent.start, parent.end)
                            linkTo(text.bottom, parent.bottom, topMargin = 2.dp)
                            width = Dimension.fillToConstraints
                        }
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}