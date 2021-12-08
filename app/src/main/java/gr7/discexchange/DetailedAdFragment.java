package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.AdsViewModel;
import gr7.discexchange.viewmodel.UserViewModel;

public class DetailedAdFragment extends Fragment {



    private Ad ad;
    private UserViewModel userViewModel;
    private AdsViewModel adsViewModel;
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

        int pos;
        String from;

        from = getArguments().getString("from");
        pos = getArguments().getInt("positionFeed");

        if (from.equals("MyFeed")) {
            ad = adsViewModel.getAds().getValue().get(pos);
        } else if (from.equals("MyAds")) {
            ad = adsViewModel.getUserAds().getValue().get(pos);
        }

        binding.setAd(ad);

        Glide.with(view).load(ad.getImageUrl()).into(binding.imageView);
    }
    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}