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

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.model.Ad;

public class StoreFragment extends Fragment {

    public StoreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView storeRecyclerView = view.findViewById(R.id.storeRecyclerView);

        storeRecyclerView.setAdapter(new AdRecycleAdapter(view.getContext(), Ad.getData()));

        storeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

}