package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr7.discexchange.adapter.PopularRecycleAdapter;
import gr7.discexchange.model.Ad;

public class PopularFragment extends Fragment {

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView popularRecyclerView = view.findViewById(R.id.popularRecyclerView);

        popularRecyclerView.setAdapter(new PopularRecycleAdapter(view.getContext(), Ad.getPopular()));

        popularRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}