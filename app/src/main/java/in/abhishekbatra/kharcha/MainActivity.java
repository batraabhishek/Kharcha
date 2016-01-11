package in.abhishekbatra.kharcha;

import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import in.abhishekbatra.kharcha.adapters.ExpenseCursorAdapter;
import in.abhishekbatra.kharcha.adapters.GeoFenceCursorAdapter;
import in.abhishekbatra.kharcha.database.DatabaseHandler;
import in.abhishekbatra.kharcha.fragments.AddExpenseDialogFragment;
import in.abhishekbatra.kharcha.fragments.DailyExpenseFragment;
import in.abhishekbatra.kharcha.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    protected GoogleApiClient mGoogleApiClient;

    private ViewPager mViewPager;
    private ExpenseCursorAdapter mExpenseCursorAdapter;
    private GeoFenceCursorAdapter mGeoFenceCursorAdapter;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private TextView mSpentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        Cursor expensesCursor = db.query(DatabaseHandler.TABLE_NAME, null, null, null, null, null, null);
        Cursor geoFenceCursor = db.query(DatabaseHandler.TABLE_GEO_FENCE, null, null, null, null, null, null);

        mGeoFenceCursorAdapter = new GeoFenceCursorAdapter(this, geoFenceCursor, 0);
        mExpenseCursorAdapter = new ExpenseCursorAdapter(this, expensesCursor, 0);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExpenseDialogFragment addExpenseDialogFragment = new AddExpenseDialogFragment();
                addExpenseDialogFragment.show(getSupportFragmentManager(), "Add Expense");
            }
        });

        initApiClient();

    }

    public void initApiClient() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 1 second, in milliseconds

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        GeofenceController.getInstance().init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        long moneySpent = databaseHandler.getTotalSpentMoney();

        mSpentTextView = new TextView(this);
        mSpentTextView.setText("SPENT Rs " + moneySpent);
        mSpentTextView.setTextColor(Color.WHITE);
        mSpentTextView.setPadding(5, 0, 32, 0);
        mSpentTextView.setTypeface(null, Typeface.BOLD);
        mSpentTextView.setTextSize(14);
        menu.add(0, 2, 1, "Total Spent").setActionView(mSpentTextView).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }

    }

    private void handleNewLocation(Location location) {
        mLocation = location;
        Log.d(TAG, location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
        if (mSpentTextView != null) {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
            long moneySpent = databaseHandler.getTotalSpentMoney();
            mSpentTextView.setText("SPENT Rs " + moneySpent);
        }

        updateExpenses();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MainFragment.newInstance();
            } else {
                return DailyExpenseFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Auto Logged";
            }
            return null;
        }
    }

    public void updateExpenses() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        Cursor expensesCursor = db.query(DatabaseHandler.TABLE_NAME, null, null, null, null, null, null);
        mExpenseCursorAdapter.changeCursor(expensesCursor);

    }

    public void updateGeoFences() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        Cursor geoFenceCursor = db.query(DatabaseHandler.TABLE_GEO_FENCE, null, null, null, null, null, null);
        mGeoFenceCursorAdapter.changeCursor(geoFenceCursor);

        mSpentTextView.setText("SPENT Rs " + databaseHandler.getTotalSpentMoney());
    }

    public ExpenseCursorAdapter getExpenseCursorAdapter() {
        return mExpenseCursorAdapter;
    }

    public GeoFenceCursorAdapter getGeoFenceCursorAdapter() {
        return mGeoFenceCursorAdapter;
    }

    public Location getLocation() {
        return mLocation;
    }
}
