package android.example.sunshineweather.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SunshinePreferences {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String location_preference = "locationKey";
    public static final String unit_preference = "UnitsKey";
    SharedPreferences sharedPreferences;

    /*
     * Human readable location string, provided by the API.  Because for styling,
     * "Mountain View" is more recognizable than 94043.
     */
    public static final String PREF_CITY_NAME = "Jamnagar";

    /*
     * In order to uniquely pinpoint the location on the map when we launch the
     * map intent, we store the latitude and longitude.
     */
    public static final Double PREF_COORD_LAT = 22.47;
    public static final Double PREF_COORD_LONG = 70.07;

    /*
     * Before you implement methods to return your REAL preference for location,
     * we provide some default values to work with.
     */
    private static final String DEFAULT_WEATHER_LOCATION = "Jamnagar";
    private static final double[] DEFAULT_WEATHER_COORDINATES = {22.47, 70.07};

    private static final String DEFAULT_MAP_LOCATION =
            "Jamnagar";

    /**
     * Helper method to handle setting location details in Preferences (City Name, Latitude,
     * Longitude)
     *
     * @param c        Context used to get the SharedPreferences
     * @param cityName A human-readable city name, e.g "Mountain View"
     * @param lat      The latitude of the city
     * @param lon      The longitude of the city
     */
    static public void setLocationDetails(Context c, String cityName, double lat, double lon) {
    }

    /**
     * Helper method to handle setting a new location in preferences.  When this happens
     * the database may need to be cleared.
     *
     * @param c               Context used to get the SharedPreferences
     * @param locationSetting The location string used to request updates from the server.
     * @param lat             The latitude of the city
     * @param lon             The longitude of the city
     */
    static public void setLocation(Context c, String locationSetting, double lat, double lon) {
    }

    /**
     * Resets the stored location coordinates.
     *
     * @param c Context used to get the SharedPreferences
     */
    static public void resetLocationCoordinates(Context c) {
    }

    /**
     * Returns the location currently set in Preferences. The default location this method
     * will return is "94043,USA", which is Mountain View, California. Mountain View is the
     * home of the headquarters of the Googleplex!
     *
     * @param context Context used to get the SharedPreferences
     * @return Location The current user has set in SharedPreferences. Will default to
     * "94043,USA" if SharedPreferences have not been implemented yet.
     */
    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     * @return true If metric display should be used
     */
    public static boolean isMetric(Context context) {
        return true;
    }

    /**
     * Returns the location coordinates associated with the location.  Note that these coordinates
     * may not be set, which results in (0,0) being returned. (conveniently, 0,0 is in the middle
     * of the ocean off the west coast of Africa)
     *
     * @param context Used to get the SharedPreferences
     * @return An array containing the two coordinate values.
     */
    public static double[] getLocationCoordinates(Context context) {
        return getDefaultWeatherCoordinates();
    }

    /**
     * Returns true if the latitude and longitude values are available. The latitude and
     * longitude will not be available until the lesson where the PlacePicker API is taught.
     *
     * @param context used to get the SharedPreferences
     * @return true if lat/long are set
     */
    public static boolean isLocationLatLonAvailable(Context context) {
        return false;
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    public static double[] getDefaultWeatherCoordinates() {
        return DEFAULT_WEATHER_COORDINATES;
    }
}