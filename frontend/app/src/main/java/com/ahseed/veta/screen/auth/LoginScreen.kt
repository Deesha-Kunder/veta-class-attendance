package com.ahseed.veta.screen.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import com.ahseed.veta.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.navigation.NavController
import com.ahseed.veta.screen.auth.AuthViewmodel
import com.ahseed.veta.ui.theme.Purple80

@Composable
fun LoginScreen(
    viewModel: AuthViewmodel = hiltViewModel(),
    navController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    val validateEmail by viewModel.emailValidate.collectAsState()
    val validatePassword by viewModel.passwordValidate.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UiEvent.Navigate -> navController.navigate(event.route) {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "appLogo",
                modifier = Modifier.size(56.dp),
                tint = Purple80
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Veta class\nAttendance System",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(26.dp))
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.background,
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        viewModel.setRole("ADMIN")
                    },

                    text = {
                        Text(
                            text = "Admin",
                            color = if (selectedTab == 0) Color.White else Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (selectedTab == 0) Purple80 else Color.Transparent
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 29.dp, vertical = 8.dp)
                        )

                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        viewModel.setRole("STUDENT")
                    },
                    text = {
                        Text(
                            "Student",
                            color = if (selectedTab == 1) Color.White else Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (selectedTab == 1) Purple80 else Color.Transparent
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 29.dp, vertical = 8.dp)
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.validateEmail(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = validateEmail != null,
                supportingText = {
                    validateEmail?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.validatePassword(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(10.dp),
                isError = validateEmail != null,
                supportingText = {
                    validatePassword?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80
                )
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Row {
                Text(text = "Don't have an account? ")
                Text(
                    text = "Signup here",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        navController.navigate("signup") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
