package in.abhishekbatra.kharcha.models;

import in.abhishekbatra.kharcha.R;

/**
 * Created by abhishek on 10/01/16 at 4:58 PM.
 *
 *     <string name="icon_food">&#xe82c;</string>
 <string name="icon_electricity">&#xe829;</string>
 <string name="icon_internet">&#xe8c0;</string>
 <string name="icon_trash">&#xe88c;</string>
 <string name="icon_games">&#xf004;</string>
 <string name="icon_movie">&#xe8d5;</string>
 <string name="icon_music">&#xe887;</string>
 <string name="icon_sports">&#xe8d9;</string>
 <string name="icon_groceries">&#xe837;</string>
 <string name="icon_electronics">&#xe8d7;</string>
 <string name="icon_furniture">&#xe8bf;</string>
 <string name="icon_taxi">&#xe83f;</string>
 <string name="icon_fuel">&#xe8c3;</string>
 <string name="icon_medical">&#xe85c;</string>
 <string name="icon_clothes">&#xe83d;</string>
 <string name="icon_other">&#xe83b;</string>
 <string name="icon_rent">&#xe8bf;</string>
 */
public enum Category {
    FOOD(0, "Food", R.string.icon_food),
    ELECTRICITY(1, "Electricity", R.string.icon_electricity),
    INTERNET(2, "Internet", R.string.icon_internet),
    TRASH(3, "Trash", R.string.icon_trash),
    GAMES(4, "Games", R.string.icon_games),
    MOVIE(5, "Movie", R.string.icon_movie),
    MUSIC(6, "Music", R.string.icon_music),
    SPORTS(7, "Sports", R.string.icon_sports),
    GROCERIES(8, "Groceries", R.string.icon_groceries),
    ELECTRONICS(9, "Electronics", R.string.icon_electronics),
    FURNITURE(10, "Furniture", R.string.icon_furniture),
    TAXI(11, "Taxi", R.string.icon_taxi),
    FUEL(12, "Fuel", R.string.icon_fuel),
    MEDICAL(13, "Medical", R.string.icon_medical),
    CLOTHES(14, "Clothes", R.string.icon_clothes),
    RENT(15, "Rent", R.string.icon_rent),
    OTHER(16, "Others", R.string.icon_other);

    private int mId;
    private String mCategoryName;
    private int mStringResource;

    Category(int id, String categoryName, int stringResource) {
        mId = id;
        mCategoryName = categoryName;
        mStringResource = stringResource;
    }

    public int getId() {
        return mId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getStringResource() {
        return mStringResource;
    }
}
