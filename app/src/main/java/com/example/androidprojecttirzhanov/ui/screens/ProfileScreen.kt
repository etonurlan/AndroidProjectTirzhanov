package com.example.androidprojecttirzhanov.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@Composable
fun ProfileScreen(viewModel: UserViewModel, navController: NavController) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current

    // Создаем ActivityResultLauncher для запроса разрешения на запись
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            downloadMockPdf(context) // Если разрешение предоставлено, скачиваем PDF
        } else {
            Toast.makeText(context, "Разрешение на запись в память отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        userProfile.avatarUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Avatar",
                modifier = Modifier.size(80.dp).clip(CircleShape)
            )
        }

        Text(text = "ФИО: ${userProfile.fullName}", style = TextStyle(fontSize = 20.sp))
        Text(text = "Должность: ${userProfile.jobTitle}")

        Button(onClick = {
            // Проверяем, есть ли разрешение на запись
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                downloadMockPdf(context)
            } else {
                // Запрашиваем разрешение на запись
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }) {
            Text("Скачать PDF резюме")
        }


        Button(onClick = { navController.navigate("edit_profile") }) {
            Text("Редактировать")
        }
    }
}