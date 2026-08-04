package io.github.yamin8000.dooz.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.common.ui.theme.Sizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTitle(
    onBackClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                shadowElevation = Sizes.Medium,
                content = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = {
                            AppText(
                                text = title,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        actions = {
                            ClickableIcon(
                                imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                                contentDescription = "",
                                onClick = onBackClick
                            )
                        }
                    )
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(
                        start = Sizes.Large,
                        end = Sizes.Large,
                        bottom = Sizes.Large,
                        top = Sizes.Medium
                    )
                    .fillMaxHeight(),
                content = content
            )
        }
    )
}