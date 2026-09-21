package com.example.maadjokes

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.WorkManager
import com.example.maadjokes.icons.notifications
import com.example.maadjokes.ui.theme.MaadJokesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaadJokesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JokeButton(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "jokes",
            "jokes",
            NotificationManager.IMPORTANCE_DEFAULT

        )
        channel.description = "Showing daily jokes notification"

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


}


@Composable
fun JokeButton(modifier: Modifier = Modifier) {


    val context = LocalContext.current
    val workManger = remember { WorkManager.getInstance(context) }

    var isDialogDismiss by remember { mutableStateOf(false) }
    if (isDialogDismiss) PermissionDenialDialog() {
        isDialogDismiss = false
    }

    val handler =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                workManger.enqueue(workerRequest)
            } else {
                isDialogDismiss = true
            }


        }



    Box(

        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(onClick = {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                handler.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {


                workManger.enqueue(workerRequest)
            }


        }) {
            Text(text = "haha me")
        }

    }
}


@SuppressLint("MissingPermission")
fun showNotification(context: Context) {

    val jokes = arrayOf(
        "An Android Developer had a new child called him Kotlin with \"Koty\" as a nickname (Koty Koty Koo!)",
        "Developers found a Java Developer who was trying to \"drink\" the language.",
        "A hungry Android Developer was trying to make an order from a restaurant, so he ordered a large \"compose\" burger.",
        "An iOS developer wanted to make a \"Swift\" career."
    )


    val link = "https://developer.android.com/compose"
    val i = Intent(Intent.ACTION_VIEW, link.toUri())
    val pendingIntent = PendingIntent.getActivity(
        context,
        101,
        i,
        PendingIntent.FLAG_IMMUTABLE
    )


    val joke = jokes.random()
    val notification = NotificationCompat
        .Builder(context, "jokes")
        .setSmallIcon(R.drawable.ic_satisfied)
        .setContentTitle(joke)
        .setStyle(NotificationCompat.BigTextStyle().bigText(joke))
        .setContentText("haha me")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

        .build()
    NotificationManagerCompat.from(context).notify(3, notification)
}


@Composable
fun PermissionDenialDialog(modifier: Modifier = Modifier, onCLick: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(onClick = {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)
            }) {
                Text("allow")
            }
        },
        dismissButton = { TextButton(onClick = onCLick) { Text("cancel") } },
        title = { Text(text = "Feature unavailable") },

        text = { Text(text = "Without notification permission, you won’t receive jokes from this app. To stay happy, please enable notifications in Settings.") },

        icon = { Icon(imageVector = notifications, contentDescription = null) }
    )
}


@Preview
@Composable
private fun PermissionDenialDialogPrev() {

}



















