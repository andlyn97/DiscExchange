package gr7.discexchange.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.Ad;

public class AdRecycleAdapter extends RecyclerView.Adapter<AdRecycleAdapter.AdViewHolder> {
    private static final String TAG = AdRecycleAdapter.class.getSimpleName();

    private List<Ad> adList;
    private LayoutInflater inflater;

    public AdRecycleAdapter(Context context, List<Ad> adList) {
        inflater = LayoutInflater.from(context);

        this.adList = adList;

    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        Log.d(TAG, "onCreateViewModel");

        View itemView = inflater.inflate(R.layout.ad_list_item, parent);

        return new AdViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder viewHolder, int position) {
        Ad adToDisplay = adList.get(position);

        //Log.d(TAG, "onBindViewHolder " + adToDisplay.getType() + " - " + position);

        viewHolder.setAd(adToDisplay);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        private TextView typeTextView;
        private ImageView thumbnailImageView;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            typeTextView = itemView.findViewById(R.id.typeTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
        }

        public void setAd(Ad adToDisplay) {
            typeTextView.setText(adToDisplay.getName());
            thumbnailImageView.setImageResource(adToDisplay.getImage());
        }

    }

}
