package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.DEViewModel;

public class DetailedAdFragment extends Fragment {



    private Ad ad;
    private DEViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity()).get(DEViewModel.class);
        int pos;
        pos = getArguments().getInt("positionMyAds");
        if(pos == 0) {
            pos = getArguments().getInt("positionFeed");
            ad = viewModel.getAds().getValue().get(pos);
        } else {
            ad = viewModel.getUserAds().getValue().get(pos);
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