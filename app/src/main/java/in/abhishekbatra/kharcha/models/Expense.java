package in.abhishekbatra.kharcha.models;

/**
 * Created by abhishek on 10/01/16 at 4:21 PM.
 */
public class Expense {

    private String mExpenseName;
    private int mAmount;
    private Category mCategory;
    private boolean mAutoAdd;
    private String mDate;

    public Expense(String expenseName, int amount, Category category, boolean autoAdd, String date) {
        mExpenseName = expenseName;
        mAmount = amount;
        mCategory = category;
        mAutoAdd = autoAdd;
        mDate = date;
    }

    public String getExpenseName() {
        return mExpenseName;
    }

    public int getAmount() {
        return mAmount;
    }

    public Category getCategory() {
        return mCategory;
    }

    public boolean isAutoAdd() {
        return mAutoAdd;
    }

    public String getDate() {
        return mDate;
    }
}
