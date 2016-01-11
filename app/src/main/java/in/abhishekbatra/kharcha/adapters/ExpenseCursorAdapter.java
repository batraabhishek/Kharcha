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
 * Created by abhishek on 10/01/16 at 5:28 PM.
 */
public class ExpenseCursorAdapter extends CursorAdapter {

    public ExpenseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_expense, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView expenseName = (TextView) view.findViewById(R.id.expense_name);
        TextView expenseAmount = (TextView) view.findViewById(R.id.expense_amount);
        TextView expenseDate = (TextView) view.findViewById(R.id.expense_date);
        TextViewPlus expenseIcon = (TextViewPlus) view.findViewById(R.id.expense_icon);

        expenseName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_EXPENSE_NAME)));
        expenseAmount.setText("Rs " + cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_EXPENSE_AMOUNT)));
        expenseDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_DATE)));

        Category[] categories = Category.values();
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_CATEGORY_ID));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.COLUMN_ID));

        expenseIcon.setText(categories[categoryId].getStringResource());

//        Drawable sectionIcon;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            sectionIcon = context.getDrawable(categories[categoryId].getStringResource());
//        } else {
//            sectionIcon = context.getResources().getDrawable(categories[categoryId].getStringResource());
//        }
//
//        expenseName.setCompoundDrawablesWithIntrinsicBounds(sectionIcon, null, null, null);




    }
}
