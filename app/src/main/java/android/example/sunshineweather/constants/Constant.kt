package android.example.sunshineweather.constants

class Constant {
    companion object {
        private const val API_KEY = "81d2f00f32eb4da4b4e45914210708"
        const val URL_PARAMS = "forecast.json?key=${API_KEY}%20&days=10&aqi=yes&alerts=yes"
        const val BASE_URL = "http://api.weatherapi.com/v1/"
        const val QUERY_PARAM = "q"
        const val MyPREFERENCES = "MyPrefs"
        const val location_preference = "locationKey"
        const val unit_preference = "UnitsKey"
        const val JAMNAGAR = "Jamnagar"
        const val CHANNEL_ID = "quarterly_daily"
        const val CHANNEL_NAME = "Weather news"
        const val CHANNEL_DESCRIPTION = "Weather updates every 6 hours"
        const val NOTIFICATION_ID = 99
    }
}