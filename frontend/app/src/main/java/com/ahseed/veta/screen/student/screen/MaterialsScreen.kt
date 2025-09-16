package com.ahseed.veta.screen.student.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahseed.veta.screen.student.modelClass.MaterialItem
import com.ahseed.veta.screen.student.viewmodel.MaterialViewModel
import com.ahseed.veta.ui.theme.primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialScreen(
     viewModel: MaterialViewModel = hiltViewModel(),
//    navController: NavController
) {
    val materials by viewModel.materials.collectAsState()
    var selectedMaterial by remember { mutableStateOf<MaterialItem?>(null) }
    val sheetState = rememberModalBottomSheetState()
    MaterialTopBar(onBackClick = {/*navController.popBackStack()*/})
    LazyColumn {
        items(materials) {material ->
            MaterialRow(material = material,
                onDownloadClick = {selectedMaterial = material})
        }
    }
    if(selectedMaterial != null){
        ModalBottomSheet(
            onDismissRequest = {selectedMaterial = null},
            sheetState = sheetState
        ) {
             Text(
                 text = "Download",
                 style = MaterialTheme.typography.titleLarge,
                 modifier = Modifier.padding(10.dp)
             )
            Text(
                text = "Are you sure want to downlpad ${selectedMaterial!!.title} ?",
                style =MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    horizontal = 16.dp, vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                OutlinedButton(onClick = {selectedMaterial = null}) {
                    Text(
                        text = "Cancel"
                    )
                }
                Button(onClick = {}) {
                    Text("Download")
                }
            }
        }
    }
}

@Composable
fun MaterialRow(material : MaterialItem,
                onDownloadClick : (MaterialItem)-> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .size(80.dp)
            .background(Color(0xFFF7F8FA), shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(width = 70.dp, height = 60.dp)
                .padding(start = 12.dp)
                .background(color = primary, shape = RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center

        ){
            Icon(
                Icons.Filled.PictureAsPdf,
                contentDescription = "PDF"
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = material.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = material.postedDate,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
        IconButton(onClick =
            {onDownloadClick(material)}){
            Icon(
                imageVector = Icons.Outlined.Download,
                contentDescription = "download"
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTopBar(onBackClick: () -> Unit){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Materials",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun MaterialTopBarPreview(){
   MaterialScreen()
}
