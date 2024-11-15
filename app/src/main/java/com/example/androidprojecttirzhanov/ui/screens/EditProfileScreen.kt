package com.example.androidprojecttirzhanov.ui.screens

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.androidprojecttirzhanov.data.UserProfile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.IOException

@Composable
fun EditProfileScreen(viewModel: UserViewModel, navController: NavController) {
    val userProfile by viewModel.userProfile.collectAsState()

    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var jobTitle by remember { mutableStateOf(userProfile.jobTitle) }
    var resumeUrl by remember { mutableStateOf(userProfile.resumeUrl) }
    var avatarUri by remember { mutableStateOf(userProfile.avatarUri) }

    var showDialog by remember { mutableStateOf(false) }

    // Context для доступа к ресурсам
    val context = LocalContext.current


    // Создаем ActivityResultLaunchers для галереи и камеры
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Сохраните bitmap и получите Uri
        bitmap?.let {
            val savedUri = saveImageToInternalStorage(context, it)
            avatarUri = savedUri
        }
    }

    // Лаунчер для запроса разрешения на камеру
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch() // Запускаем камеру после получения разрешения
        } else {

        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = avatarUri,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    showDialog = true
                }
        )

        if (showDialog) {
            showPhotoSourceDialog(
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                    showDialog = false
                },
                onCameraClick = {
                    Log.d("Dialog", "Запрос разрешения на камеру")
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                    showDialog = false
                }
            )
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("ФИО") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Должность") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = resumeUrl,
            onValueChange = { resumeUrl = it },
            label = { Text("URL резюме") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(onClick = {
            viewModel.saveUserProfile(UserProfile(fullName, avatarUri, resumeUrl, jobTitle))
            navController.navigateUp()
        }) {
            Text("Готово")
        }
    }
}

@Composable
fun showPhotoSourceDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Выберите источник фото") },
        buttons = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onGalleryClick) { Text("Галерея") }
                Button(onClick = onCameraClick) { Text("Камера") }
            }
        }
    )
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri? {
    val fileName = "avatar_${System.currentTimeMillis()}.jpg"
    val directory = context.filesDir // Используем внутреннее хранилище приложения
    val file = File(directory, fileName)

    try {
        val outputStream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return Uri.fromFile(file) // Возвращаем Uri файла
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

//fun downloadFile(url: String, context: Context) {
//    // Для Android 9 и ниже проверяем разрешение
//    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//            return
//        }
//    }
//
//    // Используем DownloadManager для Android 10 и выше
//    val request = DownloadManager.Request(Uri.parse(url))
//    request.setTitle("Resume")
//    request.setDescription("Downloading resume")
//    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resume.pdf")
//
//    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    downloadManager.enqueue(request)
//
//    // Здесь можно добавить обработку успешной загрузки
//}
//
//fun openFile(context: Context, fileUri: Uri) {
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        setDataAndType(fileUri, "application/pdf")
//        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//    }
//    context.startActivity(intent)
//}
//fun openFile(context: Context, fileUri: Uri) {
//    try {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setDataAndType(fileUri, "application/pdf")
//        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        context.startActivity(intent)
//    } catch (e: ActivityNotFoundException) {
//        Toast.makeText(context, "No application found to open PDF", Toast.LENGTH_SHORT).show()
//    }
//}
fun downloadMockPdf(context: Context) {
    val fileName = "test.pdf"
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadFolder, fileName)

    // Генерация файла, если он ещё не существует
    if (!file.exists()) {
        try {
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                fos.write(byteArrayOf(0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x34)) // Заголовок %PDF-1.4
            }
            showDownloadNotification(context, fileName)  // Показываем уведомление о завершении
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Не удалось создать файл PDF", Toast.LENGTH_SHORT).show()
        }
    } else {
        showDownloadNotification(context, fileName)  // Файл уже существует, показываем уведомление
    }
}

fun showDownloadNotification(context: Context, fileName: String) {
    val channelId = "download_channel"
    val notificationId = 1

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Download Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.stat_sys_download_done)
        .setContentTitle("Загрузка завершена")
        .setContentText("Файл $fileName сохранён в папку Загрузки.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}