package in.abhishekbatra.kharcha;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.abhishekbatra.kharcha.database.DatabaseHandler;
import in.abhishekbatra.kharcha.models.Expense;
import in.abhishekbatra.kharcha.models.NamedGeofence;

public class KharchaIntentService extends IntentService {


    private final String TAG = KharchaIntentService.class.getName();

    private SharedPreferences prefs;
    private Gson gson;

    public KharchaIntentService() {
        super("KharchaIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = getApplicationContext().getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        gson = new Gson();

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
                onError(event.getErrorCode());
            } else {
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    List<String> geofenceIds = new ArrayList<>();
                    for (Geofence geofence : event.getTriggeringGeofences()) {
                        geofenceIds.add(geofence.getRequestId());
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        onEnteredGeofences(geofenceIds);
                    }
                }
            }
        }
    }


    private void onEnteredGeofences(List<String> geofenceIds) {

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
        for (String geofenceId : geofenceIds) {
            Expense expense = null;
            String geoId = null;

            // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference
            Map<String, ?> keys = prefs.getAll();


            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String jsonString = prefs.getString(entry.getKey(), null);
                NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
                if (namedGeofence.getId().equals(geofenceId)) {
                    expense = databaseHandler.getExpenseFromGeoId(geofenceId);
                    geoId = geofenceId;
                    break;
                }
            }

            if (expense != null) {

                sendNotification(this, expense, geoId);

            }

        }
    }

    public static void sendNotification(Context context, Expense expense, String geoId) {
        String contextText = String.format("Add " + expense.getExpenseName() + " to expenses. Touch to confirm.");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent confirmNotificationIntent = new Intent(context, AddNotificationReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putString(DatabaseHandler.GEO_ID, geoId);
        confirmNotificationIntent.putExtras(bundle);
        confirmNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent addNotiPendingIntent = PendingIntent.getBroadcast(context, 0, confirmNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        confirmNotificationIntent.putExtra(DatabaseHandler.GEO_ID, geoId);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Kharcha: New Auto Log expense")
                .setContentText(contextText)
                .setContentIntent(addNotiPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }

    private void onError(int i) {
        Log.e(TAG, "Geofencing Error: " + i);
    }

}

