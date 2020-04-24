package ai.salesken.v1.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.LoginActivity;

public class SaleskenNotificationUtil {


        public static final String PRIMARY_CHANNEL = "default";
        public static final String SECONDARY_CHANNEL = "secondary_default";
        public static final String HIGH_CHANNEL = "high_default";

        private Context context;

        public SaleskenNotificationUtil(Context context) {
            this.context = context;
        }

        public NotificationCompat.Builder getBuilder(String title, String message){
            NotificationCompat.Builder mBuilder=  new NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message
                            ))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.app_icon_white);
                mBuilder.setColor(context.getResources().getColor(R.color.theme_color));
            } else {
                mBuilder.setSmallIcon(R.drawable.app_icon_white);
            }

            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.outgoing);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getResources().getString(R.string.feroz_channel_name);
                String description = context.getResources().getString(R.string.feroz_channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, name, importance);
                channel.enableLights(true);
                channel.setLightColor(context.getResources().getColor(R.color.theme_color));
                channel.enableVibration(true);
                channel.setDescription(description);
                channel.setSound(soundUri, new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
                NotificationManager notificationManager1 = context.getSystemService(NotificationManager.class);
                notificationManager1.createNotificationChannel(channel);
            }

            return mBuilder;


        }


        public static void sendNormalNotification(Context context,String title,String body){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SECONDARY_CHANNEL)
                    //.setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(body)

                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    ;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.app_icon_white);
                builder.setColor(context.getResources().getColor(R.color.theme_color));
            } else {
                builder.setSmallIcon(R.drawable.app_icon_white);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getResources().getString(R.string.feroz_channel_name);
                String description = context.getResources().getString(R.string.feroz_channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(SECONDARY_CHANNEL, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager =context. getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(4, builder.build());
        }




        public static void sendHeadsUpNotification(Context context,String title,String body){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, HIGH_CHANNEL)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    ;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.app_icon_white);
                builder.setColor(context.getResources().getColor(R.color.theme_color));
            } else {
                builder.setSmallIcon(R.drawable.app_icon_white);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getResources().getString(R.string.feroz_channel_name);
                String description = context.getResources().getString(R.string.feroz_channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(HIGH_CHANNEL, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager =context. getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(3, builder.build());
        }








      /*  public static void sendHeadsUpNotificationWithRemoteView(Context context,String title,String body){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
            RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

            notificationLayout.setTextViewText(R.id.title,title);
            notificationLayoutExpanded.setTextViewText(R.id.title,title);

            notificationLayout.setTextViewText(R.id.subtitle,body);
            notificationLayoutExpanded.setTextViewText(R.id.subtitle,body);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, HIGH_CHANNEL)
                    //.setSmallIcon(R.drawable.notification_icon)
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                    ;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.app_icon_white);
                builder.setColor(context.getResources().getColor(R.color.theme_color));
            } else {
                builder.setSmallIcon(R.drawable.app_icon_white);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getResources().getString(R.string.feroz_channel_name);
                String description = context.getResources().getString(R.string.feroz_channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(HIGH_CHANNEL, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager =context. getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(2, builder.build());
        }*/




    }