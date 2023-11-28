package com.checker.ui.listApps

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.checker.domain.AppItem
import com.checker.factory
import com.checker.ui.theme.CheckerTheme
import kotlinx.coroutines.launch

class AppsFragment : Fragment() {
    private val viewModel: AppsViewModel by viewModels { factory() }

    private lateinit var navController: NavController

    private val previewList = listOf(
        AppItem(
            0,
            Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888),
            "Qwerty",
            listOf("ServiceMeow"),
            listOf("ProviderMeow"),
            listOf("ReceiverMeow"),
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = ComposeView(requireContext()).apply {
            setContent {
                AppItemList(onItemClick = {
                    val action = AppsFragmentDirections.appsFragmentToPointsFragment(it)
                    navController.navigate(action)
                })
            }
        }

        navController = findNavController()

        return view
    }

    @Composable
    internal fun AppItemList(onItemClick: (AppItem) -> Unit) {
        CheckerTheme {
            val apps by viewModel.apps.collectAsState(initial = emptyList())
            val downloaded by viewModel.downloaded.collectAsState(initial = false)
            val scrollState = rememberLazyListState()

            Box {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f), state = scrollState) {
                        items(apps) { item ->
                            AppItemCard(item = item, onItemClick = onItemClick)
                        }
                    }

                    LaunchedEffect(Unit) {
                        viewModel.apps.collect {
                            lifecycle.coroutineScope.launch {
                                scrollState.scrollToItem(0)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.initApps()
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Update")
                    }
                }

                if (!downloaded) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopCenter)
                    )
                }
            }

        }
    }

    @Composable
    internal fun AppItemCard(item: AppItem, onItemClick: (AppItem) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onItemClick(item) },
            elevation = CardDefaults.cardElevation()
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = BitmapPainter(item.image.asImageBitmap()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    if (item.services.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Services: " + item.services.size,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    if (item.providers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Providers: " + item.providers.size,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    if (item.receivers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Receivers: " + item.receivers.size,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun AppScreen() {
        CheckerTheme {
            val scrollState = rememberLazyListState()
            Box {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f), state = scrollState) {
                        items(previewList) { item ->
                            AppItemCard(item = item, onItemClick = {})
                        }
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Update")
                    }
                }

                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}