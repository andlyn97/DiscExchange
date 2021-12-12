package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr7.discexchange.adapter.ShoppingCartRecycleAdapter;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.StoreViewModel;


public class ShoppingcartFragment extends Fragment {
    private StoreViewModel storeViewModel;
    private RecyclerView cartRV;
    private ShoppingCartRecycleAdapter cartAdapter;
    private TextView cartPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shoppingcart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRV = view.findViewById(R.id.shoppingCartRV);
        cartRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cartPrice = view.findViewById(R.id.shoppingCartPrice);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        storeViewModel.getShoppingcart().observe((LifecycleOwner) view.getContext(), x -> {
            List<Ad> cartItems = storeViewModel.getShoppingcart().getValue();
            cartAdapter = new ShoppingCartRecycleAdapter(view.getContext(), cartItems);
            cartPrice.setText("Totalt: " + calculatePrice(cartItems));
            cartRV.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();

        });

    }

    private double calculatePrice(List<Ad> items) {
        double sum = 0;
        for (Ad ad : items) {
            sum += ad.getPrice();
        }
        return sum;
    }
}