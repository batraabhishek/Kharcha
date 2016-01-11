package in.abhishekbatra.kharcha;

import android.app.Application;

import in.abhishekbatra.kharcha.database.DatabaseHandler;

/**
 * Created by abhishek on 10/01/16 at 4:08 PM.
 */
public class KharchaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.close();
    }
}
