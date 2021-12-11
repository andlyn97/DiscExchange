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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.AdsViewModel;


public class MyFeedFragment extends Fragment implements AdRecycleAdapter.OnCardListener {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference adCollectionReference;
    private List<Ad> ads;
    private List<String> adsUids;

    private RecyclerView adRecyclerView;
    private AdRecycleAdapter adAdapter;
    private ListenerRegistration firestoreListenerRegistration;

    private AdsViewModel adsViewModel;
    private static final String TAG = CreateAdFragment.class.getName();



    public MyFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        adCollectionReference = firebaseFirestore.collection("ad");
        ads = new ArrayList<>();
        adsUids = new ArrayList<>();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adRecyclerView = view.findViewById(R.id.adRecyclerView);
        adRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adsViewModel = new ViewModelProvider(requireActivity()).get(AdsViewModel.class);

        adsViewModel.getFeed().observe((LifecycleOwner) view.getContext(), test -> {
            adAdapter = new AdRecycleAdapter(view.getContext(), adsViewModel.getFeed().getValue(), this);
            adRecyclerView.setAdapter(adAdapter);
            adAdapter.notifyDataSetChanged();
        });

        FloatingActionButton fab = view.findViewById(R.id.myFeedFAB);

        fab.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "Create");
            Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuCreateAd, bundle);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCardClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionFeed", pos);
        bundle.putString("from", "MyFeed");
        Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuDetailedAd, bundle);
    }

    @Override
    public boolean onCardLongClick(int pos) {
        return false;
    }
}