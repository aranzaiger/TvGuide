package ex3.sagid.aranz.tvguide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aranz on 22-May-16.
 */

/**
 * A type of Video
 */
public class Episode extends Video{

    public Episode(JSONObject jsonObject){
        super(jsonObject);
        try {
            footer = "Season "+ jsonObject.getString("season")+ " Episode "+ jsonObject.getString("number")+
                        " air time " + jsonObject.getString("airdate") + " " + jsonObject.getString("airtime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
