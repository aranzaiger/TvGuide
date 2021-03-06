package ex3.sagid.aranz.tvguide;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aranz on 23-May-16.
 */
public class Video {
    private static final String NO_IMAGE_URL = "https://az853139.vo.msecnd.net/static/images/not-found.png";
    private static final String TAG = "[Video Class] - ";
    protected String name, footer, summary, imgURL;
    protected int id;

    public Video(JSONObject jsonObject) {
        try{
            name = jsonObject.getString("name");
            id = jsonObject.getInt("id");
            summary = jsonObject.getString("summary");

            if (jsonObject.isNull("image")){
                imgURL = NO_IMAGE_URL;
            }
            else{
                imgURL =  jsonObject.getJSONObject("image").getString("medium");
            }
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

    /**
     * Will return the List of the Videos. Video could be A show OR a series
     * @param jArray the video array
     * @param isShows boolean if it is a show
     * @return
     */
    public static List<Video> getVideos(JSONArray jArray, Boolean isShows){
        List<Video> videos = new ArrayList<>();
        try {
            if (isShows) {
                for (int i = 0; i < jArray.length(); i++) {
                    videos.add(new Show(jArray.getJSONObject(i).getJSONObject("show")));
                }
            }
            else{
                for (int i = 0; i < jArray.length(); i++) {
                    videos.add(new Episode(jArray.getJSONObject(i)));
                }
            }


        }catch(Exception e){
            Log.d(TAG,e.getMessage());
        }
        return videos;
    }

    public String getName() {
        return name;
    }



    public String getSummary() {
        return summary;
    }


    public String getImgURL() {
        return imgURL;
    }


    public int getId() {
        return id;
    }

    public String getFooter() {
        return footer;
    }
}
