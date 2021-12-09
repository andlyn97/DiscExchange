package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import gr7.discexchange.viewmodel.ChatViewModel;

public class ChatFragment extends Fragment implements ChatRecycleAdapter.OnChatRoomListener {

    private ChatViewModel chatViewModel;
    private RecyclerView recyclerView;
    private ChatRecycleAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        recyclerView = view.findViewById(R.id.chatRoomsRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL)); // Source: https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chatViewModel.getRooms().observe((LifecycleOwner) view.getContext(), x -> {
            adapter = new ChatRecycleAdapter(view.getContext(), chatViewModel.getRooms().getValue(), this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });



    }

    @Override
    public void onChatRoomClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("positionChatRoom", pos);
        Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuChatRoom, bundle);
    }

}