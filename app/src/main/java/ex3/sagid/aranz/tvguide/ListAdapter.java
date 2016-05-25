package ex3.sagid.aranz.tvguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

/**
 * Created by aranz on 22-May-16.
 */
public class ListAdapter extends BaseAdapter {

    Context mContext;

    List<Video> mVideos;
    ImageView mImageView;



    public ListAdapter(Context mContext, List<Video> mVideos) {
        this.mContext = mContext;
        this.mVideos = mVideos;
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public Video getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;


        if(v == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.info_layout , parent , false);
        }

        TextView header = (TextView) v.findViewById(R.id.infoHeaderTextView);
        TextView summery = (TextView) v.findViewById(R.id.infoInfoTextView);
        TextView footer = (TextView) v.findViewById(R.id.infoFooterTextView);
//        ImageView image = (ImageView) v.findViewById(R.id.infoImageImageView);

        Log.d("TAG", summery.toString());
        String url = mVideos.get(position).getImgURL();
        Log.d("TAG","url is: " + url);
//        if (url == "null"){
//            url = "https://az853139.vo.msecnd.net/static/images/not-found.png";
//        }

        header.setText(mVideos.get(position).getName());
        summery.setText(Html.fromHtml(mVideos.get(position).getSummary()));
        footer.setText(mVideos.get(position).getFooter());
        requestImage(url,v);

        return v;
    }

    public void requestImage(String url, View v) {
        mImageView = (ImageView) v.findViewById(R.id.infoImageImageView);

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG","failed to get image");
//                        mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mContext).addToRequestQueue(request);
    }
}