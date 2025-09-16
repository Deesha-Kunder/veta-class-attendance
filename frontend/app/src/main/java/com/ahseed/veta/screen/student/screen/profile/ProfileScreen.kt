package com.ahseed.veta.screen.student.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahseed.veta.screen.student.modelClass.Profile

@Composable
fun ProfileScreen(){
    val profile = Profile(
        id = "12345",
        name = "Ethan Bennett",
        phoneNumber = "(555) 123-4567",
        email = "ethan.bennett@email.com",
        profession = "Student",
        profileUrl="",
        joinedDate = "August 15, 2022",
        courseHours = 120
    )
    Profile(profile)
}
@Composable
fun Profile( profile: Profile
    ){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth().size(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "back"
                )
            }
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "back"
                )
            }
        }
        Column (
            modifier = Modifier.fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile pic",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                tint = Color.DarkGray
            )
            Text(
                text = "deesha",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = "ID:99999",
                style = MaterialTheme.typography.labelLarge,

            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        , modifier = Modifier.padding(8.dp))
        ProfileDetailRow(label = "Phone Number", value = profile.phoneNumber)
        ProfileDetailRow(label = "Email ID", value = profile.email)
        ProfileDetailRow(label = "Profession", value = profile.profession)
        ProfileDetailRow(label = "Joined Date", value = profile.joinedDate)
        ProfileDetailRow(label = "Allocated Course Hours", value = "${profile.courseHours} hours")

    }
}
@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column (
        modifier = Modifier.padding(8.dp),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
        HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
    }
}
@Preview(showBackground = true)
@Composable
fun previewww(){
    ProfileScreen()
}