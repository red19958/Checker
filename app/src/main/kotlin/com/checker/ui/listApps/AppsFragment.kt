package com.checker.ui.listApps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.checker.AppItem
import com.checker.factory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppsFragment : Fragment() {
    private val viewModel: AppsViewModel by viewModels { factory() }
    private lateinit var items: Flow<List<AppItem>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = ComposeView(requireContext()).apply {
            setContent {
                AppItemList(onItemClick = {})
            }
        }

        items = viewModel.apps
        return view
    }

    @Preview
    @Composable
    fun AppsScreen() {
        AppItemList(onItemClick = {})
    }

    @Composable
    fun AppItemList(onItemClick: (AppItem) -> Unit) {
        val apps by items.collectAsState(initial = emptyList())
        
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(apps) { item ->
                    AppItemCard(item = item, onItemClick = onItemClick)
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
    }

    @Composable
    fun AppItemCard(item: AppItem, onItemClick: (AppItem) -> Unit) {
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
                    painter = BitmapPainter(item.bitmap.asImageBitmap()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = item.text1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = item.text2 ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}