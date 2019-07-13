package com.bluejack.submission2.service.model.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.bluejack.submission2.BuildConfig;
import com.bluejack.submission2.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class NewReleaseAlarmManager extends BroadcastReceiver {

    private static final String TYPE_ONE_TIME = "OneTimeAlarm";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_TITLE = "title";
    private final int ID_ONETIME = 100;
    private Context context;
    private AlarmManager alarmManager;

    public NewReleaseAlarmManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public NewReleaseAlarmManager() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getNewRelease(context, "movie");
        getNewRelease(context, "tv");
    }

    public void setOneTimeAlarm(Context context) {

        Intent intent = new Intent(context, NewReleaseAlarmManager.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0);
//        if (alarmManager != null) {
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//        }

    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_Release";
        String CHANNEL_NAME = "Reminder channel";

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fiber_new_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(notifId, notification);
        }
    }

    public void cancelAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RepeatingAlarmManager.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Reminder New Release has been disabled", Toast.LENGTH_SHORT).show();
    }

    private void getNewRelease(final Context context, final String type) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/discover/" + type + "?api_key=" + BuildConfig.API_KEY + "&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject obj = new JSONObject(result);
                    JSONArray arr = obj.getJSONArray("results");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject filmObj = arr.getJSONObject(i);

                        String charSequence = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        if (type.equalsIgnoreCase("tv")) {
                            if (filmObj.getString("first_air_date").equals(charSequence)) {
                                showAlarmNotification(context, "NEW RELEASE IN TV SHOW", filmObj.getString("name") + " release today!", ID_ONETIME);
                            }
                        } else {
                            if (filmObj.getString("release_date").equals(charSequence)) {
                                showAlarmNotification(context, "NEW RELEASE IN MOVIE", filmObj.getString("title") + " release today!", ID_ONETIME);
                            }
                        }


                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
}
