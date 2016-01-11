package in.abhishekbatra.kharcha.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.abhishekbatra.kharcha.GeofenceController;
import in.abhishekbatra.kharcha.KharchaIntentService;
import in.abhishekbatra.kharcha.MainActivity;
import in.abhishekbatra.kharcha.R;
import in.abhishekbatra.kharcha.adapters.CategoryAdapter;
import in.abhishekbatra.kharcha.database.DatabaseHandler;
import in.abhishekbatra.kharcha.models.Category;
import in.abhishekbatra.kharcha.models.Expense;
import in.abhishekbatra.kharcha.models.NamedGeofence;

/**
 * Created by abhishek on 10/01/16 at 1:16 PM.
 */
public class AddExpenseDialogFragment extends DialogFragment {

    private static final String TAG = "ExpenseFragment";
    private Spinner mSpinner;
    private CategoryAdapter mCategoryAdapter;

    private EditText mExpenseName;
    private EditText mAmount;
    private View mView;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_add_fragment, null);


        mCategoryAdapter = new CategoryAdapter(getContext());

        mSpinner = (Spinner) mView.findViewById(R.id.expense_category);
        mExpenseName = (EditText) mView.findViewById(R.id.expense_name);
        mAmount = (EditText) mView.findViewById(R.id.expense_amount);

        mSpinner.setAdapter(mCategoryAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (validate()) {
                            int categoryId = mSpinner.getSelectedItemPosition();

                            Category[] categories = Category.values();
                            Category category = categories[categoryId];
                            String expenseName = mExpenseName.getText().toString();
                            int amount = Integer.parseInt(mAmount.getText().toString());


                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd hh:mm aaa", Locale.ENGLISH);
                            String strDate = sdf.format(cal.getTime());

                            Expense expense = new Expense(expenseName, amount, category, false, strDate);
                            DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getContext());
                            CheckBox autoLogBox = (CheckBox) mView.findViewById(R.id.expense_auto_log);
                            if (autoLogBox.isChecked()) {
                                // Auto log selected;
                                Location location = ((MainActivity) getActivity()).getLocation();

                                if(location ==  null) {
                                    Snackbar.make(mView, "Not able to fetch current location. Cannot save auto log expense.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    return;
                                }
                                NamedGeofence geofence = new NamedGeofence(expenseName, amount, category, location.getLatitude(), location.getLongitude());

                                databaseHandler.addGeoFenceExpense(geofence);

                                Expense expense1 = databaseHandler.getExpenseFromGeoId(geofence.getId());
//                                KharchaIntentService.sendNotification(getContext(), expense1, geofence.getId());

                                GeofenceController.getInstance().addGeofence(geofence, new GeofenceController.GeofenceControllerListener() {
                                    @Override
                                    public void onGeofencesUpdated() {
                                        Log.d(TAG, "Added geofence");
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Error adding geofence");
                                    }
                                });
                            }

                            databaseHandler.addExpense(expense);

                            dialog.dismiss();
                            ((MainActivity) getActivity()).updateExpenses();
                            ((MainActivity) getActivity()).updateGeoFences();
                        }
                    }

                });

            }
        });
        return dialog;
    }

    private boolean validate() {

        String expenseName = mExpenseName.getText().toString();
        if (expenseName.length() < 3) {
            Snackbar.make(mView, "Input valid expense name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        boolean validAmount = mAmount.getText().length() != 0 && Integer.parseInt(mAmount.getText().toString()) > 3;
        if (!validAmount) {
            Snackbar.make(mView, "Input valid expense amount", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        return true;
    }

}
