package geofence;

import android.content.Context;
import android.content.SharedPreferences;

public class SimpleGeofenceStore {

    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

    public static final String KEY_LATITUDE = "com.example.wearable.geofencing.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.example.wearable.geofencing.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.example.wearable.geofencing.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION = "com.example.wearable.geofencing.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE = "com.example.wearable.geofencing.KEY_TRANSITION_TYPE";
    public static final String KEY_PREFIX = "com.example.wearable.geofencing.KEY";

    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences.
    private static final String SHARED_PREFERENCES = "SharedPreferences";

    /**
     * Create the SharedPreferences storage with private access only.
     */
    public SimpleGeofenceStore(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Returns a stored geofence by its id, or returns null if it's not found.
     * @param id The ID of a stored geofence.
     * @return A SimpleGeofence defined by its center and radius, or null if the ID is invalid.
     */
    public SimpleGeofence getGeofence(String id) {
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE),
                INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE),
                INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS),
                INVALID_FLOAT_VALUE);
        long expirationDuration =
                mPrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                        INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object.
        if (lat != INVALID_FLOAT_VALUE
                && lng != INVALID_FLOAT_VALUE
                && radius != INVALID_FLOAT_VALUE
                && expirationDuration != INVALID_LONG_VALUE
                && transitionType != INVALID_INT_VALUE) {
            return new SimpleGeofence(id, lat, lng, radius, expirationDuration, transitionType);
        }
        // Otherwise, return null.
        return null;
    }

    /**
     * Save a geofence.
     * @param geofence The SimpleGeofence with the values you want to save in SharedPreferences.
     */
    public void setGeofence(String id, SimpleGeofence geofence) {
        // Get a SharedPreferences editor instance. Among other things, SharedPreferences
        // ensures that updates are atomic and non-concurrent.
        SharedPreferences.Editor prefs = mPrefs.edit();
        // Write the Geofence values to SharedPreferences.
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) geofence.getLatitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) geofence.getLongitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), geofence.getRadius());
        prefs.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        prefs.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes.
        prefs.commit();
    }

    /**
     * Remove a flattened geofence object from storage by removing all of its keys.
     */
    public void clearGeofence(String id) {
        SharedPreferences.Editor prefs = mPrefs.edit();
        prefs.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
        prefs.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
        prefs.remove(getGeofenceFieldKey(id, KEY_RADIUS));
        prefs.remove(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION));
        prefs.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
        prefs.commit();
    }

    /**
     * Given a Geofence object's ID and the name of a field (for example, KEY_LATITUDE), return
     * the key name of the object's values in SharedPreferences.
     * @param id The ID of a Geofence object.
     * @param fieldName The field represented by the key.
     * @return The full key name of a value in SharedPreferences.
     */
    private String getGeofenceFieldKey(String id, String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }

}
