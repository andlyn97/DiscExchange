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

import org.w3c.dom.Text;

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

        View itemView = inflater.inflate(R.layout.ad_list_item, parent, false);

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
        private TextView nameTextView;
        private TextView conditionTextView;
        private TextView colorTextView;
        private TextView inkTextView;
        private TextView wishTextView;
        private ImageView thumbnailImageView;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            nameTextView = itemView.findViewById(R.id.tvName);
            conditionTextView = itemView.findViewById(R.id.tvCondition);
            colorTextView = itemView.findViewById(R.id.tvColor);
            inkTextView = itemView.findViewById(R.id.tvInk);
            wishTextView = itemView.findViewById(R.id.tvWish);
        }

        public void setAd(Ad adToDisplay) {
            nameTextView.setText(adToDisplay.getName());
            conditionTextView.setText(adToDisplay.getCondition());
            colorTextView.setText(adToDisplay.getColor());
            thumbnailImageView.setImageResource(adToDisplay.getImage());
            inkTextView.setText(adToDisplay.getInk());
            wishTextView.setText(adToDisplay.getWish());
        }

    }

}
