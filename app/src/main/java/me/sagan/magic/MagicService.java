package me.sagan.magic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.media.session.*;
import android.media.*;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;

import java.io.File;

import android.util.Log;

public class MagicService extends HiddenCameraService {
    CameraConfig mCameraConfig;
    private MediaSession mediaSession;
    private static final int NOTIFICATION_ID = 1;

    private void showForegroundNotification(String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = new NotificationChannel("magic", "magic", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(this, "magic")
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    public MagicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("fuck", "--service created");
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "magic").mkdirs();

        mCameraConfig = new CameraConfig()
                .getBuilder(this)
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .setImageRotation(CameraRotation.ROTATION_90)
                .build();
        try {
            startCamera(mCameraConfig);
        } catch (SecurityException e) {
            Log.d("fuck", "error " + e);
        }


        mediaSession = new MediaSession(this, "MagicService");
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackState.Builder()
                .setState(PlaybackState.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());
        mediaSession.setCallback(new MediaSession.Callback() {
        });

        VolumeProvider myVolumeProvider =
                new VolumeProvider(VolumeProvider.VOLUME_CONTROL_RELATIVE, 100, 50) {
                    @Override
                    public void onAdjustVolume(int direction) {
                        //  -1 -- volume down, 1 -- volume up, 0 -- volume button released
                        Log.d("fuck", "volume press " + direction);
                        if (direction != 0) {
                            takePicture();
                        }
                    }
                };

        mediaSession.setPlaybackToRemote(myVolumeProvider);
        mediaSession.setActive(true);

        showForegroundNotification("Magic Service running");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("fuck", "--service start");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        Tool.saveImageToDCIM(imageFile, this);
    }

    @Override
    public void onCameraError(int errorCode) {
        Log.d("fuck", "service, camera error " + errorCode);
        //         ERROR_CAMERA_OPEN_FAILED = 1122;
//         ERROR_CAMERA_PERMISSION_NOT_AVAILABLE = 5472;
//        ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION = 3136;
//        ERROR_DOES_NOT_HAVE_FRONT_CAMERA = 8722;
//         ERROR_IMAGE_WRITE_FAILED = 9854;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaSession != null) {
            mediaSession.release();
        }
        stopForeground(true);
        Log.d("fuck", "--service destroy");
    }
}
