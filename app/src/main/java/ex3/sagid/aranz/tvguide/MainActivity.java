package ex3.sagid.aranz.tvguide;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[Main Activity] - ";
    protected Button findButton;
    protected ListView view;
    protected EditText findText;
    protected LinearLayout layout;
    protected boolean watchingEpisodes;
    protected List<Video> mVideo;
    protected ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listAdapter =  null; // Initialization will happen in depth
        this.watchingEpisodes = false;
        // UI Components
        this.layout = (LinearLayout) findViewById(R.id.layout);
        this.findText = (EditText) findViewById(R.id.searchText);
        this.findButton = (Button) findViewById(R.id.findButton);
        this.view = (ListView) findViewById(R.id.listView);
        this.findText.setHint("Search series...");
        removeTextFocus();
        // Android M Permission handling
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        // click on search button
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTextFocus();
                watchingEpisodes = false;
               requestShows(findText.getText().toString());
            }
        });
        // click on listView item
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!watchingEpisodes){
                    watchingEpisodes=true;
                    requestEpisodes(mVideo.get(position).getId());
                }
            }

        });

    }

    /**
     * This function will remove the focus from the input field - and will remove the Keyboard if needed
     */
    private void removeTextFocus() {
        // Remove Auto Focus from the Text Fields

        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.requestFocus();
        // Remove the Keyboard - If exists
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Make an Episode HTTP request to the API
     * @param id Series ID
     */
    private void requestEpisodes(int id){
        String url = createEpisodeURL(id);
        JsonArrayRequest request =
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response){

                                Log.d(TAG, "success: response - "+ response.toString());
                                loadData(response, false);
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Log.d(TAG, "error: msg: " +error.getMessage());
                            }
                        }
                );
        VolleyUtilSingleTone.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Generate a URL for Episodes requests
     * @param id Series id
     * @return String URL
     */
    private String createEpisodeURL(int id) {
        return "http://api.tvmaze.com/shows/" + id + "/episodes";
    }

    /**
     * Generate a URL for Series request
     * @param show Name of the Series
     * @return String URL
     */
    private String createShowURL(String show){
        return  "http://api.tvmaze.com/search/shows?q=" + show;
    }

    /**
     * HTTP request for Series
     * @param show Name of Series
     */
    private void requestShows(String show){
        String url = createShowURL(show);
        JsonArrayRequest request =
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response){
                                Log.d(TAG, "success: response - "+ response.toString());
                                loadData(response, true);
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Log.d(TAG, "error: msg: " +error.getMessage());
                            }
                        }
                );
        VolleyUtilSingleTone.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Load the Data into The list view
     * @param jArray HTTP response
     * @param isSeries Boolean - is it a series or episode request
     */
    private void loadData(JSONArray jArray, boolean isSeries){
        mVideo = Video.getVideos(jArray, isSeries);
        if(listAdapter == null){ // In case of the first population - listAdapter is null
            listAdapter = new ListAdapter(this, mVideo);
        }else{ // Update the View
            listAdapter.updateList(mVideo);
            listAdapter.notifyDataSetChanged();
        }
        view.setAdapter(listAdapter);
    }


}
