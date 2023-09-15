package io.github.caimucheng.leaf.common.ui.preferences.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.github.caimucheng.leaf.common.model.Preference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BasePreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    onClick: () -> Unit,
    onWidget: (@Composable () -> Unit)? = null
) {
    AnimatedVisibility(
        visible = preference.visible,
        enter = fadeIn(tween(220)),
        exit = fadeOut(tween(220))
    ) {
        if (preference.enabled) {
            Card(
                onClick = onClick,
                modifier = Modifier
                    .defaultMinSize(minHeight = 40.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                ConstraintLayout(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, bottom = 15.dp)
                ) {
                    val (icon, item, content) = createRefs()
                    Row(modifier = Modifier
                        .wrapContentSize()
                        .constrainAs(icon) {
                            centerVerticallyTo(parent)
                            start.linkTo(
                                parent.start,
                                margin = preference.icon?.let { 20.dp } ?: 0.dp)
                        }) {
                        preference.icon?.invoke()
                    }
                    Column(modifier = Modifier.constrainAs(item) {
                        start.linkTo(icon.end, margin = preference.icon?.let { 30.dp } ?: 16.dp)
                        end.linkTo(content.start, margin = onWidget?.let { 20.dp } ?: 0.dp)
                        centerVerticallyTo(icon)
                        width = Dimension.fillToConstraints
                    }) {
                        Text(
                            text = preference.title,
                            fontSize = 16.sp,
                            maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                            overflow = if (preference.singleLineTitle) TextOverflow.Ellipsis else TextOverflow.Clip
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = preference.summary,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        )
                    }
                    Row(modifier = Modifier.constrainAs(content) {
                        linkTo(
                            icon.end,
                            parent.end,
                            endMargin = onWidget?.let { 20.dp } ?: 16.dp,
                            bias = 1f)
                        centerVerticallyTo(icon)
                    }) {
                        onWidget?.invoke()
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .defaultMinSize(minHeight = 40.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            ) {
                ConstraintLayout(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, bottom = 15.dp)
                        .alpha(0.6f)
                ) {
                    val (icon, item, content) = createRefs()
                    Row(modifier = Modifier
                        .wrapContentSize()
                        .constrainAs(icon) {
                            centerVerticallyTo(parent)
                            start.linkTo(
                                parent.start,
                                margin = preference.icon?.let { 20.dp } ?: 0.dp)
                        }) {
                        preference.icon?.invoke()
                    }
                    Column(modifier = Modifier.constrainAs(item) {
                        start.linkTo(icon.end, margin = preference.icon?.let { 30.dp } ?: 16.dp)
                        end.linkTo(content.start, margin = onWidget?.let { 20.dp } ?: 0.dp)
                        centerVerticallyTo(icon)
                        width = Dimension.fillToConstraints
                    }) {
                        Text(
                            text = preference.title,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = preference.summary,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                    Row(modifier = Modifier.constrainAs(content) {
                        linkTo(
                            icon.end,
                            parent.end,
                            endMargin = onWidget?.let { 20.dp } ?: 16.dp,
                            bias = 1f)
                        centerVerticallyTo(icon)
                    }) {
                        onWidget?.invoke()
                    }
                }
            }
        }
    }
}