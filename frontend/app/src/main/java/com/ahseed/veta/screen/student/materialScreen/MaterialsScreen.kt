package com.ahseed.veta.screen.student.materialScreen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.screen.admin.announcementscreen.FileState
import com.ahseed.veta.ui.theme.primary
import com.ahseed.veta.utils.FileUtil
import com.ahseed.veta.utils.getTimeAgo
import kotlinx.coroutines.flow.compose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialScreen(
    viewModel: MaterialStudentViewModel = hiltViewModel(),
    navController: NavController
) {
    val materials by viewModel.materials.collectAsState()
    var selectedMaterial by remember { mutableStateOf<MaterialItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val fileState by viewModel.fileState.collectAsState()
    val selectedUrl by viewModel.selectedUrl.collectAsState()
    val context = LocalContext.current
    
    val selectedUrlForDownload by viewModel.selectedUrlForDownload.collectAsState()

    LaunchedEffect(selectedUrl) {
        selectedUrl?.let { url ->
            Log.d("Selected url :", url)
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
            viewModel.cleanSelectedFleUrl()

        }
    }
    Column {
        MaterialTopBar(onBackClick = {
            navController.popBackStack()
        })
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (fileState) {
                is FileState.Idle -> Text("No files yet")
                is FileState.Loading -> Text("Loading files")
                is FileState.Error -> Text("Unable to get the files")
                is FileState.Success -> {

                    LazyColumn {
                        items(materials) { material ->
                            MaterialRow(
                                material = material,
                                onDownloadClick = {
                                    selectedMaterial = material
                                    viewModel.setSelectedUrl(material.fileId)
                                                  },
                                onClick = {
                                    viewModel.viewFile(material.fileId)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (selectedMaterial != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedMaterial = null },
                sheetState = sheetState
            ) {
                Text(
                    text = "Download",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Are you sure want to download ${selectedMaterial!!.filename} ?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        horizontal = 16.dp, vertical = 8.dp
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick =
                            { selectedMaterial = null }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    Button(onClick = {
                        Log.d("Clicked download button","clicked")
                        selectedMaterial?.let { material ->
                            Log.d("Download", "Starting download for: ${material.filename}")
                            viewModel.selectedUrlForDownload.value?.let { selectedUrl ->
                                Log.d("Download", "Starting download for: $selectedUrlForDownload")
                                FileUtil.downloadFile(context,selectedUrl,material.filename)
                            }
                        }
                        selectedMaterial = null
                    }) {
                        Text("Download")
                    }
                }
            }
        }
    }
}

@Composable
fun MaterialRow(
    material: MaterialItem,
    onDownloadClick: (MaterialItem) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(80.dp)
            .clickable {
                Log.d("MaterialRow Click :","clicked")
                onClick() }
            .background(Color(0xFFF7F8FA), shape = RoundedCornerShape(20.dp)),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 70.dp, height = 60.dp)
                .padding(start = 12.dp)
                .background(color = primary, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                Icons.Filled.PictureAsPdf,
                contentDescription = "PDF"
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = material.filename,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = getTimeAgo(material.addedAt),
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
        IconButton(
            onClick =
                {
                    onDownloadClick(material)
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.Download,
                contentDescription = "download"
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            onBackClick()
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "back"
            )
        }
        Text(
            text = "Materials",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.DownloadForOffline,
                contentDescription = "Downloaded"
            )
        }
    }
}
