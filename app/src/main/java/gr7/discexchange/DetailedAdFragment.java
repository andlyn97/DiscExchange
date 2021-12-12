package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.AdsViewModel;
import gr7.discexchange.viewmodel.ChatViewModel;
import gr7.discexchange.viewmodel.StoreViewModel;
import gr7.discexchange.viewmodel.UserViewModel;

public class DetailedAdFragment extends Fragment {



    private Ad ad;
    private UserViewModel userViewModel;
    private AdsViewModel adsViewModel;
    private StoreViewModel storeViewModel;
    private ChatViewModel chatViewModel;
    private FragmentDetailedAdBindingImpl binding;
    public DetailedAdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_ad, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        adsViewModel = new ViewModelProvider(requireActivity()).get(AdsViewModel.class);
        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);

        Button chatWithBtn = view.findViewById(R.id.chatWithBtn);
        TextView wish = view.findViewById(R.id.wishesTextView);

        int posFeed, posAds, posStore;
        String from;

        from = getArguments().getString("from");
        posFeed = getArguments().getInt("positionFeed");
        posAds = getArguments().getInt("positionMyAds");
        posStore = getArguments().getInt("positionStore");

        switch (from) {
            case "MyFeed":
                ad = adsViewModel.getFeed().getValue().get(posFeed);
                userViewModel.getUsers().getValue().forEach(user -> {
                    if(user.getUid().equals(ad.getUserUid())) {
                        wish.setText("Ønsker: " + ad.getWish());
                        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
                        chatWithBtn.setText("Kontakt " + user.getName());
                    }
                });
                break;
            case "MyAds":
                ad = adsViewModel.getUserAds().getValue().get(posAds);
                userViewModel.getUsers().getValue().forEach(user -> {
                    if(user.getUid().equals(ad.getUserUid())) {
                        wish.setText("Ønsker: " + ad.getWish());
                    }
                });
                break;
            case "StoreAds":
                ad = storeViewModel.getStoreAds().getValue().get(posStore);
                chatWithBtn.setText("Legg til i handlekurv");
                wish.setText("Pris: " + ad.getPrice());
                break;
        }

        binding.setAd(ad);

        Glide.with(view).load(ad.getImageUrl()).into(binding.imageView);

        chatWithBtn.setOnClickListener(v -> {

            if (from.equals("MyFeed")) {
                AtomicBoolean fetchedMessages = new AtomicBoolean(false);
                chatViewModel.getRooms().getValue().forEach(room -> {
                    if(room.getAdUid().equals(ad.getUid())) {
                        chatViewModel.getMessagesFromFirestore(room.getRoomUid());
                        fetchedMessages.set(true);
                        return;
                    }
                });

                if(!fetchedMessages.get()) {
                    chatViewModel.addMessageRoomToFirestore(ad);
                }

                Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuChatRoom);
            } else if (from.equals("StoreAds")) {
                storeViewModel.addToShoppingcart(ad);
                Log.d("Maome", "shoppingcart: " + storeViewModel.getShoppingcart());
                Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.menuShoppingCart);
            }
        });


        String loggedInUser = FirebaseAuth.getInstance().getUid();
        if(ad.getUserUid().equals(loggedInUser)) {
            chatWithBtn.setVisibility(View.GONE);
        }
    }
    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}