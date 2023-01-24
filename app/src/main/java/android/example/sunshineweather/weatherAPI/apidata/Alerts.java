
package android.example.sunshineweather.weatherAPI.apidata;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Alerts implements Serializable {

    @SerializedName("alert")
    @Expose
    private List<Object> alert = null;

    public List<Object> getAlert() {
        return alert;
    }

    public void setAlert(List<Object> alert) {
        this.alert = alert;
    }

}
