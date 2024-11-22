package com.example.androidprojecttirzhanov.ui.screens

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.androidprojecttirzhanov.presentation.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.compose.AsyncImage
import com.example.androidprojecttirzhanov.NotificationReceiver
import com.example.androidprojecttirzhanov.data.UserProfile
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.IOException
import java.util.Calendar

@Composable
fun EditProfileScreen(viewModel: UserViewModel, navController: NavController) {
    val userProfile by viewModel.userProfile.collectAsState()

    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var jobTitle by remember { mutableStateOf(userProfile.jobTitle) }
    var resumeUrl by remember { mutableStateOf(userProfile.resumeUrl) }
    var avatarUri by remember { mutableStateOf(userProfile.avatarUri) }
    var favoriteTime by remember { mutableStateOf(userProfile.favoriteTime) }
    var isTimeValid by remember { mutableStateOf(true) }

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
        OutlinedTextField(
            value = favoriteTime,
            onValueChange = {
                favoriteTime = it
                isTimeValid = validateTime(it)
                if (isTimeValid) {
                    setNotificationAtTime(context, it) // Устанавливаем уведомление только если время корректное
                }
            },
            label = { Text("Время любимой пары (HH:mm)") },
            isError = !isTimeValid,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Выбрать время",
                    tint = Color.Blue,
                    modifier = Modifier.clickable {
                        showTimePicker(context) { selectedTime ->
                            favoriteTime = selectedTime
                            setNotificationAtTime(
                                context,
                                selectedTime
                            ) // Устанавливаем уведомление
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            )
        )

        if (!isTimeValid) {
            Text("Введите корректное время в формате HH:mm", color = Color.Red, fontSize = 12.sp)
        }

        Button(onClick = {
            viewModel.saveUserProfile(UserProfile(fullName, avatarUri, resumeUrl, jobTitle, favoriteTime))
            navController.navigateUp()
        }) {
            Text("Готово")
        }
    }
}

fun validateTime(time: String): Boolean {
    return time.matches(Regex("^([01]?\\d|2[0-3]):[0-5]\\d$"))
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(String.format("%02d:%02d", hour, minute))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}
fun setNotificationAtTime(context: Context, time: String) {
    val calendar = Calendar.getInstance()

    // Парсим строку времени "HH:mm"
    val timeParts = time.split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].toInt()

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Если время уже прошло на сегодняшний день, установим на следующий день
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(Calendar.DATE, 1)
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Создаем PendingIntent для уведомления
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Устанавливаем будильник
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
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