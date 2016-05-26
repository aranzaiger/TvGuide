package ex3.sagid.aranz.tvguide;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by aranz on 22-May-16.
 */
public class ListAdapter extends BaseAdapter {

    protected Context mContext;

    protected List<Video> mVideos;

    protected int lastPosition; // For COOL Animation :)



    public ListAdapter(Context mContext, List<Video> mVideos) {
        this.mContext = mContext;
        this.mVideos = mVideos;
        this.lastPosition = -1;

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
        NetworkImageView thumbNail = (NetworkImageView) v.findViewById(R.id.infoImageImageView);
        String url = mVideos.get(position).getImgURL();
        thumbNail.setImageUrl(url, VolleyUtilSingleTone.getInstance(mContext).getImageLoader());
        header.setText(mVideos.get(position).getName());
        summery.setText(Html.fromHtml(mVideos.get(position).getSummary()));
        footer.setText(mVideos.get(position).getFooter());
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        lastPosition = position;
        return v;
    }


    // Added Ability to change the List - and then able to call notifyDataSetChanged();
    public void updateList(List<Video> updatedList){
        this.mVideos = updatedList;
    }
}
