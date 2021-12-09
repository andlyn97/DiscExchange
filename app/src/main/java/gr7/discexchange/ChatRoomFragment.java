package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import gr7.discexchange.adapter.ChatRoomRecycleAdapter;
import gr7.discexchange.model.Message;
import gr7.discexchange.viewmodel.ChatViewModel;

public class ChatRoomFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private RecyclerView recyclerView;
    private ChatRoomRecycleAdapter adapter;
    private TextView userYouChatWithTV;
    private TextInputEditText inputMessage;
    private AppCompatButton submitMessage;

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
        userYouChatWithTV.setText("Du chatter med " + chatViewModel.getFromUsername());

        inputMessage = view.findViewById(R.id.chatRoomInputMessage);
        submitMessage = view.findViewById(R.id.chatRoomSubmitMessage);

        submitMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = new Message();
                message.setMessage(inputMessage.getText().toString());
                message.setSentAt(String.valueOf(System.currentTimeMillis()));
                message.setFromUserUid(FirebaseAuth.getInstance().getUid());
                message.setFromUser(chatViewModel.getUser().getValue());

                FirebaseFirestore
                        .getInstance()
                        .collection("messageRoom")
                        .document(chatViewModel.getCurrentRoomUid())
                        .collection("messages")
                        .add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(!documentReference.get().isSuccessful()) {
                            return;
                        }
                        DocumentSnapshot docSnap = documentReference.get().getResult();
                        Message message = docSnap.toObject(Message.class);

                        chatViewModel.addMessage(message);
                    }
                });
            }
        });

        chatViewModel.getMessages().observe((LifecycleOwner) view.getContext(), x -> {
            adapter = new ChatRoomRecycleAdapter(view.getContext(), chatViewModel.getMessages().getValue());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });


    }
}