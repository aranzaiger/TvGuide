package ex3.sagid.aranz.tvguide;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aranz on 22-May-16.
 */
public class Show extends Video{

    public Show(JSONObject jsonObject) {
        super(jsonObject);
        footer = "";
    }

}
