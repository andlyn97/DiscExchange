package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr7.discexchange.adapter.ChatRecycleAdapter;
import gr7.discexchange.viewmodel.ChatViewModel;

public class ChatFragment extends Fragment implements ChatRecycleAdapter.OnChatRoomListener {
    private ChatViewModel chatViewModel;
    private RecyclerView recyclerView;
    private TextView emptyView;
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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)); // Source: https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        emptyView = view.findViewById(R.id.chatRoomsEmpty);

        chatViewModel.getRooms().observe(getViewLifecycleOwner(), x -> {
            // Inspiration source: https://stackoverflow.com/questions/28217436/how-to-show-an-empty-view-with-a-recyclerview
            if(chatViewModel.getRooms().getValue() != null) {
                if(chatViewModel.getRooms().getValue().size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
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