package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.google.android.material.tabs.TabLayout;

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;
import gr7.discexchange.viewmodel.DEViewModel;

public class MyAdsFragment extends Fragment implements AdRecycleAdapter.OnCardListener {

    private FragmentDetailedAdBindingImpl binding;
    private DEViewModel viewModel;
    private RecyclerView adRecyclerView;
    private AdRecycleAdapter adAdapter;
    private TabLayout tabLayout;

    public MyAdsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(DEViewModel.class);
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_ads, container, false);
        //return binding.getRoot();
        return inflater.inflate(R.layout.fragment_my_ads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adRecyclerView = view.findViewById(R.id.myAdRecyclerView);
        adRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
                switch(tab.getPosition()) {
                    case 0:
                        viewModel.getUserAds(false).observe((LifecycleOwner) view.getContext(), x -> {
                            adAdapter = new AdRecycleAdapter(view.getContext(), viewModel.getUserAds(false).getValue(), this);
                        });
                        break;
                    case 1:
                        viewModel.getUserAds(true).observe((LifecycleOwner) view.getContext(), x -> {
                            adAdapter = new AdRecycleAdapter(view.getContext(), viewModel.getUserAds(true).getValue(), this);
                        });
                        break;
                }
                adRecyclerView.setAdapter(adAdapter);
                adAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onCardClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionMyAds", pos);
        Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuDetailedAd, bundle);
    }
}