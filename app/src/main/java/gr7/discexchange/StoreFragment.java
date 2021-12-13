package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.viewmodel.StoreViewModel;

public class StoreFragment extends Fragment implements AdRecycleAdapter.OnCardListener{
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference adCollectionReference;
    private RecyclerView adRecyclerView;
    private AdRecycleAdapter storeAdAdapter;
    private StoreViewModel storeViewModel;

    public StoreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        adCollectionReference = firebaseFirestore.collection("adStore");

        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adRecyclerView = view.findViewById(R.id.storeRecyclerView);
        adRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);

        storeViewModel.getStoreAds().observe((LifecycleOwner) view.getContext(), test -> {
            storeAdAdapter = new AdRecycleAdapter(view.getContext(), storeViewModel.getStoreAds().getValue(), this);
            adRecyclerView.setAdapter(storeAdAdapter);
            storeAdAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onCardClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionStoreAds", pos);
        bundle.putString("from", "StoreAds");
        Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuDetailedAd, bundle);
    }

    @Override
    public boolean onCardLongClick(int pos) {
        return false;
    }
}