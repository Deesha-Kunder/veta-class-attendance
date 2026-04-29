package com.ahseed.veta.screen.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahseed.veta.data.modelclass.AttendanceItem

@Composable
fun ReportScreen() {
    Report()
}

@Composable
fun Report() {
    val attendanceList = listOf(
        AttendanceItem("2024-07-01", "08:00 AM", "04:00 PM", "8", "0"),
        AttendanceItem("2024-07-02", "08:05 AM", "03:30 PM", "7.5", "0.5"),
        AttendanceItem("2024-07-03", "08:10 AM", "04:10 PM", "8", "0"),
        AttendanceItem("2024-07-01", "08:00 AM", "04:00 PM", "8", "0"),
        AttendanceItem("2024-07-02", "08:05 AM", "03:30 PM", "7.5", "0.5"),
        AttendanceItem("2024-07-03", "08:10 AM", "04:10 PM", "8", "0"),
        AttendanceItem("2024-07-01", "08:00 AM", "04:00 PM", "8", "0"),
        AttendanceItem("2024-07-02", "08:05 AM", "03:30 PM", "7.5", "0.5"),
        AttendanceItem("2024-07-03", "08:10 AM", "04:10 PM", "8", "0"),
        AttendanceItem("2024-07-01", "08:00 AM", "04:00 PM", "8", "0"),
        AttendanceItem("2024-07-02", "08:05 AM", "03:30 PM", "7.5", "0.5"),
        AttendanceItem("2024-07-03", "08:10 AM", "04:10 PM", "8", "0"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back"
                )
            }
            Text(
                text = "Report Screen",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Total Hours",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "120",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Column {
                        Text(
                            text = "Remaining Hours",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "80",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "This month",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Attendance Details", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 10.dp)
        )
        LazyColumn {
            items(attendanceList){ item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(  10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Check-in: ${item.checkIn}", style = MaterialTheme.typography.bodyMedium)
                        Text(item.date, fontSize = 12.sp, color = Color.Gray)
                        Text("Check-out: ${item.checkOut}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Text("${item.totalHours} hours", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun previeww() {
    Report()
}