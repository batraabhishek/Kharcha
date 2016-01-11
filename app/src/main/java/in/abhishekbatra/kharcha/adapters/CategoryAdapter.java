package in.abhishekbatra.kharcha.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import in.abhishekbatra.kharcha.R;
import in.abhishekbatra.kharcha.models.Category;

/**
 * Created by abhishek on 10/01/16 at 2:52 PM.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private Category[] mCategories;

    public CategoryAdapter(Context context) {
        mContext = context;
        mCategories = Category.values();
    }

    @Override
    public int getCount() {
        return mCategories.length;
    }

    @Override
    public Category getItem(int i) {
        return mCategories[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        CategoryHolder holder;

        if (view != null) {
            holder = (CategoryHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.item_spinner_category, viewGroup, false);
            holder = new CategoryHolder();
        }

        Category category = getItem(i);

        holder.categoryName = (TextView) view.findViewById(R.id.category_text);
        holder.categoryIcon = (TextView) view.findViewById(R.id.category_icon);


        holder.categoryName.setText(category.getCategoryName());
        holder.categoryIcon.setText(mContext.getString(category.getStringResource()));

        view.setTag(holder);
        return view;
    }

    public static class CategoryHolder {
        TextView categoryName;
        TextView categoryIcon;
    }
}
