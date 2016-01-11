package in.abhishekbatra.kharcha.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.abhishekbatra.kharcha.models.Category;
import in.abhishekbatra.kharcha.models.Expense;
import in.abhishekbatra.kharcha.models.NamedGeofence;

/**
 * Created by abhishek on 10/01/16 at 3:48 PM.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "expenses.db";

    // Expenses Table
    public static final String TABLE_NAME = "expenses";

    public static final String COLUMN_EXPENSE_NAME = "expense_name";
    public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_AUTO_ADDED = "is_auto_added";
    public static final String COLUMN_DATE = "date";
    // Geo-fence Table
    public static final String TABLE_GEO_FENCE = "geofence";
    public static final String COLUMN_GEO_NAME = "expense_name";
    public static final String COLUMN_GEO_AMOUNT = "expense_amount";
    public static final String COLUMN_GEO_CATEGORY_ID = "category_id";
    public static final String GEO_ID = "geo_id";

    public static final String COLUMN_ID = "_id";
    private static DatabaseHandler mInstance;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG, "Creating database");

        String expenseQuery = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT,  %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT)";
        expenseQuery
                = String.format(expenseQuery, TABLE_NAME, COLUMN_ID, COLUMN_EXPENSE_NAME, COLUMN_EXPENSE_AMOUNT, COLUMN_CATEGORY_ID, COLUMN_AUTO_ADDED, COLUMN_DATE);
        String geoFenceQuery = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s INTEGER, %s TEXT)";
        geoFenceQuery = String.format(geoFenceQuery, TABLE_GEO_FENCE, COLUMN_ID, COLUMN_GEO_NAME, COLUMN_GEO_AMOUNT, COLUMN_GEO_CATEGORY_ID, GEO_ID);
        sqLiteDatabase.execSQL(expenseQuery);
        sqLiteDatabase.execSQL(geoFenceQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GEO_FENCE);
        onCreate(sqLiteDatabase);
    }

    public void addExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_NAME, expense.getExpenseName());
        values.put(COLUMN_EXPENSE_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY_ID, expense.getCategory().getId());
        values.put(COLUMN_DATE, expense.getDate());
        values.put(COLUMN_AUTO_ADDED, expense.isAutoAdd() == true ? 1 : 0);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addGeoFenceExpense(NamedGeofence geoFence) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_GEO_NAME, geoFence.getName());
        values.put(COLUMN_GEO_AMOUNT, geoFence.getAmount());
        values.put(COLUMN_GEO_CATEGORY_ID, geoFence.getCategory().getId());
        values.put(GEO_ID, geoFence.getId());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_GEO_FENCE, null, values);
        db.close();
    }

    public void deleteExpenseItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        db.close();
    }

    public void deleteGeoFenceItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GEO_FENCE, COLUMN_ID + "=" + id, null);
        db.close();
    }

    public Expense getExpenseFromGeoId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GEO_FENCE, null, GEO_ID + "='" + id + "'", null, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GEO_NAME));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GEO_AMOUNT));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GEO_CATEGORY_ID));
            Category[] categories = Category.values();
            Category category = categories[categoryId];

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd hh:mm aaa");
            String strDate = sdf.format(cal.getTime());
            Expense expense = new Expense(name, amount, category, true, strDate);

            return expense;
        }
        cursor.close();
        db.close();
        return null;
    }

    public long getTotalSpentMoney() {

        String query = "select sum(%s) from %s";
        query = String.format(query, COLUMN_EXPENSE_AMOUNT, TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        long amount;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
            amount = c.getLong(0);
        else
            amount = -1;
        c.close();
        return amount;
    }
}
