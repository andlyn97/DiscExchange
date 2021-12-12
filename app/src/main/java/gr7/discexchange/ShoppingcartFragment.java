package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.adapter.ShoppingCartRecycleAdapter;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.StoreViewModel;
import gr7.discexchange.viewmodel.UserViewModel;


public class ShoppingcartFragment extends Fragment {
    private StoreViewModel storeViewModel;
    private UserViewModel userViewModel;
    private RecyclerView cartRV;
    private ShoppingCartRecycleAdapter cartAdapter;
    private TextView cartPrice;
    private AppCompatButton buyBtn;
    private AppCompatButton clearBtn;

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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        storeViewModel.getShoppingcart().observe((LifecycleOwner) view.getContext(), x -> {
            List<Ad> cartItems = storeViewModel.getShoppingcart().getValue();
            cartAdapter = new ShoppingCartRecycleAdapter(view.getContext(), cartItems);
            cartPrice.setText("Totalt: " + calculatePrice(cartItems));
            cartRV.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();

        });

        buyBtn = view.findViewById(R.id.shoppingCartBuy);
        buyBtn.setOnClickListener(v -> {
            double storeCredit = userViewModel.getUser().getValue().getStoreCredit();
            double cartTotal = calculatePrice(storeViewModel.getShoppingcart().getValue());
            if(storeCredit <= cartTotal) {
                userViewModel.payForCart(cartTotal);
                storeViewModel.setShoppingcart(new ArrayList<>());
            } else {
                // Not enough storecredit
                Log.d("ShoppingCartDebug", "Not enough credit, you have: " + storeCredit + " and you need: " + cartTotal);
            }
        });

        clearBtn = view.findViewById(R.id.shoppingCartClear);
        clearBtn.setOnClickListener(v -> storeViewModel.setShoppingcart(new ArrayList<>()));

    }

    private double calculatePrice(List<Ad> items) {
        double sum = 0;
        for (Ad ad : items) {
            sum += ad.getPrice();
        }
        return sum;
    }
}