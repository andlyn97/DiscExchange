package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.DEViewModel;


public class MyFeedFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference adCollectionReference;
    private List<Ad> ads;
    private List<String> adsUids;

    private RecyclerView adRecyclerView;
    private AdRecycleAdapter adAdapter;
    private ListenerRegistration firestoreListenerRegistration;

    private DEViewModel viewModel;



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

        viewModel = new ViewModelProvider(requireActivity()).get(DEViewModel.class);

        setupRecyclerView(view);
    }



    private void createFirestoreReadListner() {
        /*adCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot adDocumentSnapshot : task.getResult()) {
                        Ad ad = adDocumentSnapshot.toObject(Ad.class);
                        ad.setUid(adDocumentSnapshot.getId());
                        ads.add(ad);
                        adsUids.add(ad.getUid());
                    }
                    adAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Debug12", "Error:" + task.getException());
                }
            }
        });*/
        // TODO: Gjøre om denne så den bruker viewmodel fremfor en liste?
        firestoreListenerRegistration = adCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }

                for(DocumentChange adDocumentChange : value.getDocumentChanges()) {
                    Ad ad = adDocumentChange.getDocument().toObject(Ad.class);
                    ad.setUid(adDocumentChange.getDocument().getId());

                    int pos = adsUids.indexOf(ad.getUid());

                    switch (adDocumentChange.getType()) {
                        case ADDED:
                            ads.add(ad);
                            adsUids.add(ad.getUid());
                            adAdapter.notifyItemInserted(ads.size()-1);
                            break;
                        case REMOVED:
                            ads.remove(pos);
                            adsUids.remove(pos);
                            adAdapter.notifyItemRemoved(pos);
                            break;
                        case MODIFIED:
                            ads.set(pos, ad);
                            adAdapter.notifyItemChanged(pos);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        createFirestoreReadListner();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(firestoreListenerRegistration != null) {
            firestoreListenerRegistration.remove();
        }

    }

    private void setupRecyclerView(View view) {
        adRecyclerView = view.findViewById(R.id.adRecyclerView);

        adAdapter = new AdRecycleAdapter(view.getContext(), ads);

        adRecyclerView.setAdapter(adAdapter);

        adRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }



}