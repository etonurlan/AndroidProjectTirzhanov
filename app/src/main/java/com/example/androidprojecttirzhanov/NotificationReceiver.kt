package com.example.androidprojecttirzhanov


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Создаем уведомление
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем Intent для открытия приложения
        val openAppIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Создаем уведомление
        val builder = NotificationCompat.Builder(context, "time_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Время любимой пары!")
            .setContentText("Пора просматривать информацию о любимой паре.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // Здесь мы добавляем PendingIntent для перехода в приложение
            .setAutoCancel(true)

        // Публикуем уведомление
        notificationManager.notify(1, builder.build())
    }
}