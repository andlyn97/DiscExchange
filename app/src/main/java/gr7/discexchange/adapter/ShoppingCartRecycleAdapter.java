package gr7.discexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.StoreViewModel;

public class ShoppingCartRecycleAdapter extends RecyclerView.Adapter<ShoppingCartRecycleAdapter.ShoppingCartViewHolder> {
    private List<Ad> cartItems;
    private LayoutInflater layoutInflater;
    private StoreViewModel storeViewModel;

    public ShoppingCartRecycleAdapter(Context context, List<Ad> cartItems ) {
        layoutInflater = LayoutInflater.from(context);
        this.cartItems = cartItems;
        this.storeViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(StoreViewModel.class);
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = layoutInflater.inflate(R.layout.cart_list_item, parent, false);

        return new ShoppingCartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Ad ad = cartItems.get(position);
        holder.setCartItem(ad);
    }

    @Override
    public int getItemCount() {
        if(cartItems.size() == 0) {
            return 0;
        }
       return cartItems.size();
    }

    public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView roundedImageView;
        private TextView discNameTV;
        private TextView discPriceTV;
        private AppCompatButton deleteItemBtn;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.shoppingCartImageView);
            discNameTV = itemView.findViewById(R.id.shoppingCartDiscName);
            discPriceTV = itemView.findViewById(R.id.shoppingCartItemPrice);
            deleteItemBtn = itemView.findViewById(R.id.shoppingCartDeleteItem);

        }

        public void setCartItem(Ad ad) {
            Glide.with(itemView.getContext())
                    .load(ad.getImageUrl())
                    .into(roundedImageView);
            discNameTV.setText(ad.getName());
            discPriceTV.setText(formatDoubleToString(ad.getPrice()));
            deleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItems.remove(getAdapterPosition());
                    storeViewModel.setShoppingcart(cartItems);
                }
            });
        }

        private String formatDoubleToString(double num) {
            // https://stackoverflow.com/questions/8895337/how-do-i-limit-the-number-of-decimals-printed-for-a-double
            DecimalFormat format = new DecimalFormat("#.##");
            return format.format(num);
        }


    }

}
