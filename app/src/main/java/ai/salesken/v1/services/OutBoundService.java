package ai.salesken.v1.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.utils.SaleskenNotificationUtil;

public class OutBoundService extends Service {
    private static final String TAG = "OutBoundService";
    private SaleskenNotificationUtil saleskenNotificationUtil;
    public OutBoundService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case SaleskenIntent.START_FOREGROUND:
                    startForegroundService();
                    break;
                case SaleskenIntent.STOP_FOREGROUND:
                    stopForegroundService();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    private void startForegroundService() {
        Intent intent = new Intent(this, DialerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = saleskenNotificationUtil.getBuilder("Hello","Calling"+" ..");
        mBuilder.setUsesChronometer(true);
        mBuilder.setContentIntent(pendingIntent);
        Intent stopIntent = new Intent(this, OutBoundService.class);
        stopIntent.setAction(SaleskenIntent.STOP_FOREGROUND);
        PendingIntent stopPlayIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        mBuilder.addAction(R.mipmap.app_icon,"Hang Up",stopPlayIntent);
        startForeground(1, mBuilder.build());
    }
}
