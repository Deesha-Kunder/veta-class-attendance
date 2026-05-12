package com.ahseed.veta.screen.student.report

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahseed.veta.data.modelclass.AttendanceItem
import com.ahseed.veta.utils.formatDate
import com.ahseed.veta.utils.formatTime
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil


@Composable
fun ReportScreen(
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getMySessions()
    }
    val attendanceRes by viewModel.sessions.collectAsState()
    val sessions = attendanceRes?.attendanceSessions ?: emptyList()
    val message by viewModel.message.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val recentSessions = remember(sessions) {
        val today = LocalDate.now()
        sessions.filter {
            if (it.date == null) return@filter false
            val sessionDate = LocalDate.parse(it.date)
            sessionDate >= today.minusDays(6)
        }.sortedBy {
            LocalDate.parse(it.date)
        }
    }
    val graphData = recentSessions.map {
        (it.durationMinutes ?: 0) / 60.0f
    }
    val entries = graphData.mapIndexed { index, hours ->
        FloatEntry(x = index.toFloat(), y = hours)
    }
    val chartModel = remember(entries) {
        entryModelOf(entries)
    }
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")

    val dayLabels = recentSessions.map {

        LocalDate.parse(it.date).format(dateFormatter)
    }

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
                .height(450.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Total Hours",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = String.format("%.2f", attendanceRes?.totalCompletedHours ?: 0.0),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Column {
                        Text(
                            text = "Remaining Hours",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = String.format("%.2f", attendanceRes?.remainingHours ?: 0.0),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Last 7 Days Attendance",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (graphData.isNotEmpty()) {
                    val chart = lineChart(
                        axisValuesOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = 4f)
                    )

                    Chart(
                        chart = columnChart(
                            axisValuesOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = 4f)
                        ),
                        model = entryModelOf(entries),
                        startAxis = rememberStartAxis(
                            valueFormatter = { value, _ -> "${value.toInt()}h" },
                            itemPlacer = remember {
                                AxisItemPlacer.Vertical.default(maxItemCount = 5) // 0,1,2,3,4
                            }
                        ),
                        bottomAxis = rememberBottomAxis(
                            valueFormatter = { value, _ ->
                                dayLabels.getOrNull(value.toInt()) ?: ""
                            },
                            itemPlacer = remember {
                                AxisItemPlacer.Horizontal.default(
                                    spacing = 1,
                                    addExtremeLabelPadding = true
                                )
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )
                } else {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "No graph data available"
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Attendance Details", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        when {
            loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            sessions.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No session found",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(sessions) { item ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                            ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    Text(
                                        text = formatDate(item.date),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "Check-in : ${formatTime(item.checkInTime)}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Check-out : ${formatTime(item.checkOutTime)}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {

                                    Text(
                                        text = "${item.durationMinutes ?: 0}",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "minutes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}