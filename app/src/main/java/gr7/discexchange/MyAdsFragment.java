package gr7.discexchange;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;

public class MyAdsFragment extends Fragment {

    private FragmentDetailedAdBindingImpl binding;

    public MyAdsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_ads, container, false);
        return binding.getRoot();
    }



}