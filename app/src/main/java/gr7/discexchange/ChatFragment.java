package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.adapter.ChatRecycleAdapter;
import gr7.discexchange.model.Message;
import gr7.discexchange.model.MessageRoom;

public class ChatFragment extends Fragment implements ChatRecycleAdapter.OnChatRoomListener {

    private MutableLiveData<List<MessageRoom>> rooms;
    private RecyclerView recyclerView;
    private ChatRecycleAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rooms = new MutableLiveData<>();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMessageRooms();

        recyclerView = view.findViewById(R.id.chatRoomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        rooms.observe((LifecycleOwner) view.getContext(), x -> {
            adapter = new ChatRecycleAdapter(view.getContext(), rooms.getValue(), this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });


    }

    @NonNull
    private void getMessageRooms() {
        ArrayList<MessageRoom> rooms1 = new ArrayList<>();
        FirebaseFirestore
                .getInstance()
                .collection("messageRoom")
                .whereEqualTo("name", "Marcus_Andreas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
                            Log.d("TAG", "onEvent: " + error.getMessage());
                        }

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            if(documentSnapshot != null) {
                                MessageRoom room = documentSnapshot.toObject(MessageRoom.class);
                                rooms1.add(room);
                            }

                        }

                        rooms.postValue(rooms1);
                    }
                });

    }

    @Override
    public void onChatRoomClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionChatRoom", pos);
        Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuChatRoom, bundle);
    }

}