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

public class PopularRecycleAdapter extends RecyclerView.Adapter<PopularRecycleAdapter.PopularViewHolder> {
    private static final String TAG = PopularRecycleAdapter.class.getSimpleName();

    private List<Ad> popList;
    private LayoutInflater inflater;

    public PopularRecycleAdapter(Context context, List<Ad> popList) {
        inflater = LayoutInflater.from(context);

        this.popList = popList;

    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        Log.d(TAG, "onCreateViewModel");

        View itemView = inflater.inflate(R.layout.popular_list_item, parent, false);

        return new PopularViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder viewHolder, int position) {
        Ad popularToDisplay = popList.get(position);

        //Log.d(TAG, "onBindViewHolder " + adToDisplay.getType() + " - " + position);

        viewHolder.setPopular(popularToDisplay);
    }

    @Override
    public int getItemCount() {
        return popList.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView manifacturer;
        private ImageView thumbnailImageView;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            name = itemView.findViewById(R.id.name);
            manifacturer = itemView.findViewById(R.id.manifacturer);
        }

        public void setPopular(Ad popularToDisplay) {
            name.setText(popularToDisplay.getName());
            manifacturer.setText(popularToDisplay.getBrand());
            thumbnailImageView.setImageResource(popularToDisplay.getImage());
        }

    }

}
