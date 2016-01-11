package in.abhishekbatra.kharcha.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import in.abhishekbatra.kharcha.R;
import in.abhishekbatra.kharcha.database.DatabaseHandler;
import in.abhishekbatra.kharcha.models.Category;
import in.abhishekbatra.kharcha.views.TextViewPlus;

/**
 * Created by abhishek on 10/01/16 at 8:56 PM.
 */
public class GeoFenceCursorAdapter extends CursorAdapter {

    public GeoFenceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_geo_fence, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView geoFenceName = (TextView) view.findViewById(R.id.geo_fence_name);
        TextView geoFenceAmount = (TextView) view.findViewById(R.id.geo_fence_amount);
        TextViewPlus expenseIcon = (TextViewPlus) view.findViewById(R.id.expense_icon);

        geoFenceName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_GEO_NAME)));
        geoFenceAmount.setText("Rs " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_GEO_AMOUNT)));

        Category[] categories = Category.values();
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_GEO_CATEGORY_ID));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_ID));

        expenseIcon.setText(categories[categoryId].getStringResource());


        view.findViewById(R.id.delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete this row
                DatabaseHandler databaseHandler = DatabaseHandler.getInstance(context);
                databaseHandler.deleteGeoFenceItem(id);
                SQLiteDatabase db = databaseHandler.getReadableDatabase();
                Cursor geoFenceCursor = db.query(DatabaseHandler.TABLE_GEO_FENCE, null, null, null, null, null, null);
                GeoFenceCursorAdapter.this.changeCursor(geoFenceCursor);
            }
        });
    }
}
