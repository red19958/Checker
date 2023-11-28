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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.checker.domain.AppItem
import com.checker.domain.PointItem
import com.checker.factory
import com.checker.ui.theme.CheckerTheme
import org.mozilla.universalchardet.UniversalDetector
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.zip.ZipFile


class PointsFragment : Fragment() {
    private val viewModel: PointsViewModel by viewModels { factory() }
    private val args by navArgs<PointsFragmentArgs>()
    private lateinit var appItem: AppItem

    private val previewList = listOf(
        PointItem(
            "ServiceMeow",
            emptyList(),
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        appItem = args.appItem

        viewModel.extractPoints(appItem)

        val view = ComposeView(requireContext()).apply {
            setContent {
                PointsList(onItemClick = ::sendSignal)
            }
        }

        return view
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun PointsList(onItemClick: (PointItem) -> Unit) {
        CheckerTheme {
            val points by viewModel.points.collectAsState(initial = emptyList())

            Column(modifier = Modifier.fillMaxSize()) {

                TopAppBar(
                    title = {
                        Text(
                            text = appItem.name
                        )
                    },
                )

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            Text(text = item.name, Modifier.padding(16.dp))
            Text(text = item.intents.toString(), Modifier.padding(16.dp))

            Button(
                onClick = { onItemClick(item) },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.End)
            ) {
                Text(text = "Check")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun PointsScreen() {
        CheckerTheme {
            Column(modifier = Modifier.fillMaxSize()) {

                TopAppBar(
                    title = {
                        Text(
                            text = "Meow"
                        )
                    },
                )

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
        try {
            val packageManager: PackageManager = requireActivity().packageManager

            val apps = packageManager.getInstalledApplications(
                PackageManager.GET_META_DATA
            )

            val packageName = apps[2].packageName

            val appInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            val appPath = appInfo.sourceDir

            val zipFile = ZipFile(appPath)
            val manifestPath = "AndroidManifest.xml"

            val manifestEntry = zipFile.getEntry(manifestPath)

            if (manifestEntry != null) {
                val inputStream = BufferedInputStream(zipFile.getInputStream(manifestEntry))
                val detector = UniversalDetector(null)
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1 && !detector.isDone) {
                    detector.handleData(buffer, 0, bytesRead)
                }

                detector.dataEnd()

                val detectedCharset = detector.detectedCharset
                if (detectedCharset != null) {
                    val stringBuilder = StringBuilder()
                    val iS = BufferedInputStream(zipFile.getInputStream(manifestEntry))
                    val reader = InputStreamReader(iS, detectedCharset)
                    val bufferedReader = BufferedReader(reader)
                    var line: String? = bufferedReader.readLine()

                    while (line != null) {
                        stringBuilder.append(line).append("\n")
                        line = bufferedReader.readLine()
                    }

                    println("Manifest:$stringBuilder")
                } else {
                    println("Encoding not detected.")
                }

                inputStream.close()
            } else {
                println("Manifest file not found in the APK.")
            }

            zipFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}