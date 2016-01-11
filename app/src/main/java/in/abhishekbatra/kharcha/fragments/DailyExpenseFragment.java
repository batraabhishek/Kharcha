package in.abhishekbatra.kharcha.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import in.abhishekbatra.kharcha.MainActivity;

/**
 * Created by abhishek on 10/01/16 at 11:39 AM.
 */
public class DailyExpenseFragment extends SmoothListFragment {

    public static Fragment newInstance() {
        DailyExpenseFragment dailyExpenseFragment = new DailyExpenseFragment();
        return dailyExpenseFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(((MainActivity)getActivity()).getGeoFenceCursorAdapter());
        setEmptyText("Your auto log expenses will live here");
    }
}
