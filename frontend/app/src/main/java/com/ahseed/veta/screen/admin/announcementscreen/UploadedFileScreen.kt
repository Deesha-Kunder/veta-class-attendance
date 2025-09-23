package com.ahseed.veta.screen.admin.announcementscreen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.ui.theme.primary
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadedFileScreen(
    viewModel: UploadViewmodel = hiltViewModel(),
    navController: NavController
) {
    val materials by viewModel.materials.collectAsState()
    val fileState by viewModel.fileState.collectAsState()
    val selectedUrl by viewModel.selectedUrl.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getFilesByAdminId()
    }
    LaunchedEffect(selectedUrl) {
        selectedUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
            viewModel.cleanSelectedFleUrl()

        }
    }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        MaterialTopBar(onBackClick = {
            navController.popBackStack()
        })
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
                                onClick = { viewModel.viewFile(material.fileId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MaterialRow(
    material: MaterialItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .size(80.dp)
            .background(Color(0xFFF7F8FA), shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
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
                text = material.fileName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = material.createdAt,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                onBackClick
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "back"
            )
        }
        Text(
            text = "Uploaded files",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
    }
}
