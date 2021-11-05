package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr7.discexchange.adapter.ChatRecycleAdapter;
import gr7.discexchange.adapter.ChatRoomRecycleAdapter;
import gr7.discexchange.viewmodel.ChatViewModel;

public class ChatRoomFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private RecyclerView recyclerView;
    private ChatRoomRecycleAdapter adapter;
    private TextView userYouChatWithTV;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        recyclerView = view.findViewById(R.id.chatRoomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        userYouChatWithTV = view.findViewById(R.id.userYouChatWith);
        userYouChatWithTV.setText(chatViewModel.getFromUsername());

        chatViewModel.getMessages().observe((LifecycleOwner) view.getContext(), x -> {
            adapter = new ChatRoomRecycleAdapter(view.getContext(), chatViewModel.getMessages().getValue());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}