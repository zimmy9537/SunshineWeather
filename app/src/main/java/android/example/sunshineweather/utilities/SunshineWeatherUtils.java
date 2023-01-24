package android.example.sunshineweather.utilities;

import android.example.sunshineweather.R;
import android.example.sunshineweather.weatherAPI.apidata.Current;
import android.example.sunshineweather.weatherAPI.apidata.Day;
import android.example.sunshineweather.weatherAPI.apidata.Hour;

public final class SunshineWeatherUtils {

    private static final String LOG_TAG = SunshineWeatherUtils.class.getSimpleName();

    public static String getTemperaturePerHour(Hour hour,boolean isMetric)
    {
        if(isMetric)
        {
            return String.valueOf(hour.getTempC()).concat("°");
        }
        else
        {
            return String.valueOf(hour.getTempF()).concat("°");
        }
    }


    public static String getTemperatureFeelsLike(Current current,boolean isMetric)
    {
        if(isMetric)
        {
            return String.valueOf(current.getFeelslikeC()).concat("°");
        }
        else
        {
            return String.valueOf(current.getFeelslikeF()).concat("°");
        }
    }

    public static String getTemperatureMinimum(Day day,boolean isMetric)
    {
        if(isMetric)
        {
            return String.valueOf(day.getMintempC()).concat("°");
        }
        else
        {
            return String.valueOf(day.getMintempF()).concat("°");
        }
    }

    public static String getTemperatureMaximum(Day day,boolean isMetric)
    {
        if(isMetric)
        {
            return String.valueOf(day.getMaxtempC()).concat("°");
        }
        else
        {
            return String.valueOf(day.getMaxtempF()).concat("°");
        }
    }

    public static int getImageResource(int isDay, int weatherCode) {
        if (isDay == 1)//is day
        {
            switch (weatherCode) {
                case 1000:
                    return R.drawable.d_113;
                case 1003:
                    return R.drawable.d_116;
                case 1006:
                    return R.drawable.d_119;
                case 1009:
                    return R.drawable.d_122;
                case 1030:
                    return R.drawable.d_143;
                case 1063:
                    return R.drawable.d_176;
                case 1069:
                    return R.drawable.d_179;
                case 1072:
                    return R.drawable.d_185;
                case 1087:
                    return R.drawable.d_200;
                case 1114:
                    return R.drawable.d_227;
                case 1117:
                    return R.drawable.d_230;
                case 1135:
                    return R.drawable.d_248;
                case 1147:
                    return R.drawable.d_260;
                case 1150:
                    return R.drawable.d_263;
                case 1153:
                    return R.drawable.d_266;
                case 1168:
                    return R.drawable.d_281;
                case 1171:
                    return R.drawable.d_284;
                case 1180:
                    return R.drawable.d_293;
                case 1183:
                    return R.drawable.d_296;
                case 1186:
                    return R.drawable.d_299;
                case 1189:
                    return R.drawable.d_302;
                case 1192:
                    return R.drawable.d_305;
                case 1195:
                    return R.drawable.d_308;
                case 1198:
                    return R.drawable.d_311;
                case 1201:
                    return R.drawable.d_314;
                case 1204:
                    return R.drawable.d_317;
                case 1207:
                    return R.drawable.d_320;
                case 1210:
                    return R.drawable.d_323;
                case 1213:
                    return R.drawable.d_326;
                case 1216:
                    return R.drawable.d_329;
                case 1219:
                    return R.drawable.d_332;
                case 1222:
                    return R.drawable.d_335;
                case 1225:
                    return R.drawable.d_338;
                case 1237:
                    return R.drawable.d_350;
                case 1240:
                    return R.drawable.d_353;
                case 1243:
                    return R.drawable.d_356;
                case 1246:
                    return R.drawable.d_359;
                case 1249:
                    return R.drawable.d_362;
                case 1252:
                    return R.drawable.d_365;
                case 1255:
                    return R.drawable.d_368;
                case 1258:
                    return R.drawable.d_371;
                case 1261:
                    return R.drawable.d_374;
                case 1264:
                    return R.drawable.d_377;
                case 1273:
                    return R.drawable.d_386;
                case 1276:
                    return R.drawable.d_389;
                case 1279:
                    return R.drawable.d_392;
                case 1282:
                    return R.drawable.d_395;
            }
        }
        else
        {
            switch (weatherCode)
            {
                case 1000:
                    return R.drawable.n_113;
                case 1003:
                    return R.drawable.n_116;
                case 1006:
                    return R.drawable.n_119;
                case 1009:
                    return R.drawable.n_122;
                case 1030:
                    return R.drawable.n_143;
                case 1063:
                    return R.drawable.n_176;
                case 1069:
                    return R.drawable.n_179;
                case 1072:
                    return R.drawable.n_185;
                case 1087:
                    return R.drawable.n_200;
                case 1114:
                    return R.drawable.n_227;
                case 1117:
                    return R.drawable.n_230;
                case 1135:
                    return R.drawable.n_248;
                case 1147:
                    return R.drawable.n_260;
                case 1150:
                    return R.drawable.n_263;
                case 1153:
                    return R.drawable.n_266;
                case 1168:
                    return R.drawable.n_281;
                case 1171:
                    return R.drawable.n_284;
                case 1180:
                    return R.drawable.n_293;
                case 1183:
                    return R.drawable.n_296;
                case 1186:
                    return R.drawable.n_299;
                case 1189:
                    return R.drawable.n_302;
                case 1192:
                    return R.drawable.n_305;
                case 1195:
                    return R.drawable.n_308;
                case 1198:
                    return R.drawable.n_311;
                case 1201:
                    return R.drawable.n_314;
                case 1204:
                    return R.drawable.n_317;
                case 1207:
                    return R.drawable.n_320;
                case 1210:
                    return R.drawable.n_323;
                case 1213:
                    return R.drawable.n_326;
                case 1216:
                    return R.drawable.n_329;
                case 1219:
                    return R.drawable.n_332;
                case 1222:
                    return R.drawable.n_335;
                case 1225:
                    return R.drawable.n_338;
                case 1237:
                    return R.drawable.n_350;
                case 1240:
                    return R.drawable.n_353;
                case 1243:
                    return R.drawable.n_356;
                case 1246:
                    return R.drawable.n_359;
                case 1249:
                    return R.drawable.n_362;
                case 1252:
                    return R.drawable.n_365;
                case 1255:
                    return R.drawable.n_368;
                case 1258:
                    return R.drawable.n_371;
                case 1261:
                    return R.drawable.n_374;
                case 1264:
                    return R.drawable.n_377;
                case 1273:
                    return R.drawable.n_386;
                case 1276:
                    return R.drawable.n_389;
                case 1279:
                    return R.drawable.n_392;
                case 1282:
                    return R.drawable.n_395;
            }
        }
        return 0;
    }
}