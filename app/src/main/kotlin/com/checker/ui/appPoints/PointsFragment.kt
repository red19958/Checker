package com.checker.ui.appPoints

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.checker.domain.AppItem
import com.checker.domain.PointItem
import com.checker.factory
import com.checker.ui.theme.CheckerTheme
import java.io.IOException
import java.util.zip.ZipFile


class PointsFragment : Fragment() {
    private val viewModel: PointsViewModel by viewModels { factory() }
    private val args by navArgs<PointsFragmentArgs>()
    private lateinit var appItem: AppItem

    private val previewList = listOf(
        PointItem(
            "Youtube"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        appItem = args.appItem

        val view = ComposeView(requireContext()).apply {
            setContent {
                PointsList(onItemClick = ::sendSignal)
            }
        }

        return view
    }

    @Composable
    internal fun PointsList(onItemClick: (PointItem) -> Unit) {
        CheckerTheme {
            val points = listOf(PointItem(appItem.name))

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(points) { item ->
                        PointItemCard(item = item, onItemClick = onItemClick)
                    }
                }

                Button(
                    onClick = ::getManifest, modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "get")
                }
            }
        }
    }

    @Composable
    fun PointItemCard(item: PointItem, onItemClick: (PointItem) -> Unit) {
        Text(text = item.name)
    }

    @Preview
    @Composable
    fun PointsScreen() {
        CheckerTheme {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(previewList) { item ->
                        PointItemCard(item = item, onItemClick = {})
                    }
                }

                Button(
                    onClick = ::getManifest, modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "get")
                }
            }
        }
    }


    private fun sendSignal(item: PointItem) {
        Log.d("Sent Signal", item.toString())
    }


    private fun getManifest() {
        val pm = requireActivity().packageManager
        val apps = pm.getInstalledApplications(
            PackageManager.GET_META_DATA or PackageManager.GET_SHARED_LIBRARY_FILES
        )

        val name = apps[0].publicSourceDir

        try {
            val apk = ZipFile(name)
            val manifest = apk.getEntry("AndroidManifest.xml")
            if (manifest != null) {
                Log.d("ManifestGetter", "Manifest size = " + manifest.size)
                val stream = apk.getInputStream(manifest)
                val buffer = ByteArray(stream.available())
                stream.read(buffer)
                val manifestString = String(buffer, Charsets.UTF_8)
                Log.d("ManifestContent", manifestString)
                stream.close()
            }
            apk.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}