package gr7.discexchange.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.Ad;

public class AdRecycleAdapter extends RecyclerView.Adapter<AdRecycleAdapter.AdViewHolder> {
    private static final String TAG = AdRecycleAdapter.class.getSimpleName();

    private List<Ad> adList;
    private LayoutInflater inflater;
    private OnCardListener onCardListener;

    public AdRecycleAdapter(Context context, List<Ad> adList, OnCardListener onCardListener) {
        inflater = LayoutInflater.from(context);

        this.adList = adList;
        this.onCardListener = onCardListener;

    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        Log.d(TAG, "onCreateViewModel");

        View itemView = inflater.inflate(R.layout.ad_list_item, parent, false);

        return new AdViewHolder(itemView, onCardListener);
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

    public class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView conditionTextView;
        private TextView colorTextView;
        private TextView inkTextView;
        private TextView wishTextView;
        private ImageView thumbnailImageView;
        private OnCardListener onCardListener;

        public AdViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            nameTextView = itemView.findViewById(R.id.tvName);
            conditionTextView = itemView.findViewById(R.id.tvCondition);
            colorTextView = itemView.findViewById(R.id.tvColor);
            inkTextView = itemView.findViewById(R.id.tvInk);
            wishTextView = itemView.findViewById(R.id.tvWish);

            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        public void setAd(Ad adToDisplay) {
            nameTextView.setText("Navn: " + adToDisplay.getName());
            conditionTextView.setText("Tilstand: " + String.valueOf(adToDisplay.getCondition()) + "/10");
            colorTextView.setText("Farge: " + adToDisplay.getColor());
            inkTextView.setText("Ink: " + adToDisplay.getInk());
            wishTextView.setText("Ønsker: " + adToDisplay.getWish());

            if(adToDisplay.getImageUrl() != null) {
                Glide.with(thumbnailImageView.getContext())
                        .load(adToDisplay.getImageUrl())
                        .into(thumbnailImageView);
            }
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener {
        void onCardClick(int pos);
    }

}
