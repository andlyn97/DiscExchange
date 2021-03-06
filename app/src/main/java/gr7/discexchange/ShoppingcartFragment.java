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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
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
    private TextView currentStoreCredit;
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

        currentStoreCredit = view.findViewById(R.id.shoppingCartCurrentStoreCredit);
        cartRV = view.findViewById(R.id.shoppingCartRV);
        cartRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cartPrice = view.findViewById(R.id.shoppingCartPrice);

        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe((LifecycleOwner) view.getContext(), x -> {
            currentStoreCredit.setText("Din saldo: " + formatDoubleToString(userViewModel.getUser().getValue().getStoreCredit()));
        });

        storeViewModel.getShoppingcart().observe((LifecycleOwner) view.getContext(), x -> {
            List<Ad> cartItems = storeViewModel.getShoppingcart().getValue();
            cartAdapter = new ShoppingCartRecycleAdapter(view.getContext(), cartItems);
            cartPrice.setText("Totalt: " + formatDoubleToString(calculateCartTotal()));
            cartRV.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();

        });

        buyBtn = view.findViewById(R.id.shoppingCartBuy);
        buyBtn.setOnClickListener(v -> {
            List<Ad> cartItems = storeViewModel.getShoppingcart().getValue();
            if(cartItems == null || cartItems.size() == 0) {
                Toast.makeText(view.getContext(),"Handlekurven er tom", Toast.LENGTH_LONG).show();
                return;
            }
            double storeCredit = userViewModel.getUser().getValue().getStoreCredit();
            double cartTotal = calculateCartTotal();
            if(storeCredit >= cartTotal) {
                userViewModel.payForCart(storeCredit - cartTotal);
                storeViewModel.setShoppingcart(new ArrayList<>());
                storeViewModel.setCartToArchived(cartItems);
            } else {
                Toast.makeText(view.getContext(),"Ikke nok butikk kreditt (" + storeCredit + "), du trenger " + (cartTotal-storeCredit) + " flere butikk kreditt!", Toast.LENGTH_LONG).show();
            }
        });

        clearBtn = view.findViewById(R.id.shoppingCartClear);
        clearBtn.setOnClickListener(v -> storeViewModel.setShoppingcart(new ArrayList<>()));

    }

    private double calculateCartTotal() {
        List<Ad> cartItems = storeViewModel.getShoppingcart().getValue();
        double sum = 0;
        for (Ad ad : cartItems) {
            sum += ad.getPrice();
        }
        return sum;
    }

    private String formatDoubleToString(double num) {
        // https://stackoverflow.com/questions/8895337/how-do-i-limit-the-number-of-decimals-printed-for-a-double
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(num);
    }

}