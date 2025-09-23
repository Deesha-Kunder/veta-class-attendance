package com.ahseed.veta.screen.admin.announcementscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahseed.veta.ui.theme.Purple40
import com.ahseed.veta.ui.theme.Purple80
import com.ahseed.veta.utils.FileUtil

@Composable
fun AnnouncementScreen(
    viewmodel: UploadViewmodel = hiltViewModel(),
    navController: NavController
) {
    var announcementText by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedFileUri = it
                selectedFileName = FileUtil.getFileNameFromUri(context, uri)
            }
        }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back"
                )
            }
            Text(
                text = "Announcement",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = {
                navController.navigate("uploaded_file_screen")
            }) {
                Icon(
                    imageVector = Icons.Filled.Upload,
                    contentDescription = "Uploaded"
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = selectedFileName.ifEmpty { announcementText },
                onValueChange = { announcementText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(5.dp),
                placeholder = { Text(text = "Write your Announcement here") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    filePickerLauncher.launch("application/pdf")
                },
                modifier = Modifier.wrapContentWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40
                )
            )
            {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = "Upload file"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Upload File")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                selectedFileUri?.let { uri ->
                    viewmodel.uploadFromUri(context, uri)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple80
            )
        ) {
            Text(
                text = "Post Announcement",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}