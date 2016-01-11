package in.abhishekbatra.kharcha.fragments;

import android.os.Build;
import android.support.v4.app.ListFragment;

/**
 * Created by abhishek on 10/01/16 at 11:41 AM.
 */
public class SmoothListFragment extends ListFragment {

    @Override
    public void setListShown(boolean shown) {
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) && isResumed()) {
            super.setListShown(shown);
        } else {
            setListShownNoAnimation(shown);
        }
    }
}
