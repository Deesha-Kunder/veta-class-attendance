package com.ahseed.veta.screen.admin.registerStudent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HowToReg
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
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.ui.theme.Purple80
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStudentScreen(
    viewModel: RegisterStudentViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var studentName by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var courseHour by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }

    var showDatePicker by remember{mutableStateOf(false)}
    var selectedDate by remember{mutableStateOf("")}

    val todayMillis = Calendar.getInstance().timeInMillis
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis
    )

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            if(message == "Registered Successfully"){
                studentName =""
                studentEmail =""
                courseHour =""
                profession =""
                batch=""
                selectedDate=""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Register Student",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.HowToReg,
                    contentDescription = "Registered Students"
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(
                value = studentName,
                onValueChange = {studentName = it},
                label = { Text(text = "Student Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = studentEmail,
                onValueChange = {studentEmail = it},
                label = {Text(text = "Student Email")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = courseHour,
                onValueChange = {courseHour = it},
                label = {Text(text = "Course hour")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = profession,
                onValueChange = {profession = it},
                label = {Text(text = "Profession")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = batch,
                onValueChange = {batch = it},
                label = {Text(text = "Batch")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                label = {Text(text = "Joined Date")},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "DatePicker"
                    )
                }
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                                datePickerState.selectedDateMillis?.let { millis->
                                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                                    selectedDate = formatter.format(millis)
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val request = RegisterStudent(
                        name = studentName,
                        email = studentEmail,
                        profession = profession,
                        courseHour = courseHour.toIntOrNull()?:0,
                        batch = batch,
                        joinedDate = selectedDate
                    )
                    viewModel.registerStudent(request)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80
                )
            ) {
                Text(text = "Register",
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}