package in.abhishekbatra.kharcha.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import in.abhishekbatra.kharcha.MainActivity;
import in.abhishekbatra.kharcha.adapters.ExpenseCursorAdapter;
import in.abhishekbatra.kharcha.database.DatabaseHandler;

/**
 * Created by abhishek on 10/01/16 at 11:39 AM.
 */
public class MainFragment extends SmoothListFragment {

    public static Fragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    /**
     * Attach to list view once the view hierarchy has been created.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(((MainActivity)getActivity()).getExpenseCursorAdapter());
        setEmptyText("Your expenses will live here");
    }
}
