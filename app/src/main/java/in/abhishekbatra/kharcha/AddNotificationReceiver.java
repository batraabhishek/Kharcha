package in.abhishekbatra.kharcha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import in.abhishekbatra.kharcha.database.DatabaseHandler;
import in.abhishekbatra.kharcha.models.Expense;

/**
 * Created by abhishek on 11/01/16 at 1:28 AM.
 */
public class AddNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Adding notification", Toast.LENGTH_SHORT).show();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(context);
        String geoId = intent.getExtras().getString(DatabaseHandler.GEO_ID);

        Toast.makeText(context, geoId, Toast.LENGTH_SHORT).show();
        Expense expense = databaseHandler.getExpenseFromGeoId(geoId);
        databaseHandler.addExpense(expense);
    }
}
