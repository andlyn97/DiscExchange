package gr7.discexchange.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.AdsViewModel;

public class AdRecycleAdapter extends RecyclerView.Adapter<AdRecycleAdapter.AdViewHolder> {
    private static final String TAG = AdRecycleAdapter.class.getSimpleName();

    private List<Ad> adList;
    private LayoutInflater inflater;
    private OnCardListener onCardListener;
    private TabLayout.OnTabSelectedListener onTabSelectedListener;
    private AdsViewModel adsViewModel;

    public AdRecycleAdapter(Context context, List<Ad> adList, OnCardListener onCardListener) {
        inflater = LayoutInflater.from(context);

        this.adList = adList;
        this.onCardListener = onCardListener;
        this.adsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AdsViewModel.class);

    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = inflater.inflate(R.layout.ad_list_item, parent, false);

        return new AdViewHolder(itemView, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder viewHolder, int position) {
        Ad adToDisplay = adList.get(position);
        viewHolder.setAd(adToDisplay);
    }

    @Override
    public int getItemCount() {
        if(adList == null) {
            return 0;
        }
        return adList.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
            itemView.setOnLongClickListener(v -> {
                if (adList.get(getAdapterPosition()).getUserUid().equals(FirebaseAuth.getInstance().getUid()) && adList.get(getAdapterPosition()).getArchived() == null) {
                    int pos = getAdapterPosition();

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext());
                    builder.setTitle("Innstillinger")
                            .setMessage("Vil du endre eller arkivere annonse?")
                            .setPositiveButton("Endre", (dialog, which) -> {
                                // Reroute til edit
                                Bundle bundle = new Bundle();
                                bundle.putInt("pos", pos);
                                bundle.putString("from", "Edit");
                                Navigation.findNavController((Activity) v.getContext(), R.id.navHostFragment).navigate(R.id.notMenuUpdateAd, bundle);
                            })
                            .setNegativeButton("Arkiver", (dialog, which) -> {
                                // Arkiver og reset
                                String uid = adsViewModel.getUserAds().getValue().get(getAdapterPosition()).getUid();
                                FirebaseFirestore.getInstance().collection("ad").document(uid).update("archived", String.valueOf(System.currentTimeMillis()));
                            })
                            .show()
                    ;
                    return true;
                }

                return false;

            });
        }

        public void setAd(Ad adToDisplay) {
            nameTextView.setText("Navn: " + adToDisplay.getName());
            conditionTextView.setText("Tilstand: " + String.valueOf(adToDisplay.getCondition()) + "/10");
            colorTextView.setText("Farge: " + adToDisplay.getColor());
            inkTextView.setText("Ink: " + adToDisplay.getInk());
            if(adToDisplay.getWish() == null || adToDisplay.getWish().equals("")) {
                wishTextView.setText("Pris: " + adToDisplay.getPrice());
            } else if(adToDisplay.getPrice() == 0) {
                wishTextView.setText("??nsker: " + adToDisplay.getWish());
            }

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

        @Override
        public boolean onLongClick(View v) {
            return onCardListener.onCardLongClick(getAdapterPosition());
        }
    }

    public interface OnCardListener {
        void onCardClick(int pos);
        boolean onCardLongClick(int pos);
    }

}
