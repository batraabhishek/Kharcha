package in.abhishekbatra.kharcha.models;

import android.support.annotation.NonNull;

import com.google.android.gms.location.Geofence;

import java.util.UUID;

public class NamedGeofence implements Comparable {

    // region Properties

    private String mId;
    private String mName;
    private int mAmount;
    private Category mCategory;
    private double mLatitude;
    private double mLongitude;
    private float mRadius;

    public NamedGeofence(String name, int amount, Category category, double latitude, double longitude) {
        mName = name;
        mAmount = amount;
        mCategory = category;
        mLatitude = latitude;
        mLongitude = longitude;
        mRadius = 500;
        mId = UUID.randomUUID().toString();
    }

    public Geofence geofence() {

        return new Geofence.Builder()
                .setRequestId(mId)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(mLatitude, mLongitude, mRadius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }


    @Override
    public int compareTo(@NonNull Object another) {
        NamedGeofence other = (NamedGeofence) another;
        return mName.compareTo(other.getName());
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public float getRadius() {
        return mRadius;
    }

    public int getAmount() {
        return mAmount;
    }

    public Category getCategory() {
        return mCategory;
    }
}
