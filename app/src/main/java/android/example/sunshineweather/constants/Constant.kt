package android.example.sunshineweather.constants

class Constant {
    companion object {
        private const val API_KEY = "81d2f00f32eb4da4b4e45914210708"
        const val URL_PARAMS = "forecast.json?key=${API_KEY}%20&days=10&aqi=yes&alerts=yes"
        const val BASE_URL = "http://api.weatherapi.com/v1/"
        const val QUERY_PARAM = "q"
    }
}