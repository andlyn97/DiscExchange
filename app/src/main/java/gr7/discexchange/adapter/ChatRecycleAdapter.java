package gr7.discexchange.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.MessageRoom;
import gr7.discexchange.viewmodel.ChatViewModel;

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ChatViewHolder> {
    private LayoutInflater inflater;
    private List<MessageRoom> rooms;
    private OnChatRoomListener onChatRoomListener;

    public ChatRecycleAdapter(Context context, List<MessageRoom> rooms, OnChatRoomListener onChatRoomListener) {
        this.inflater = LayoutInflater.from(context);
        this.rooms = rooms;
        this.onChatRoomListener = onChatRoomListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.room_list_item, parent, false);

        return new ChatRecycleAdapter.ChatViewHolder(itemView, onChatRoomListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        MessageRoom room = rooms.get(position);
        holder.setRoom(room);
    }

    @Override
    public int getItemCount() {
        if(rooms == null) {
            return 0;
        }
        return rooms.size();
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RoundedImageView userPicture;
        private TextView fromUser;
        private TextView lastMessage;
        private ChatViewModel chatViewModel;

        private OnChatRoomListener onChatRoomListener;

        public ChatViewHolder(@NonNull View itemView, OnChatRoomListener onChatRoomListener) {
            super(itemView);
            userPicture = itemView.findViewById(R.id.roomListItemUserPicture);
            fromUser = itemView.findViewById(R.id.roomListItemFromUser);
            lastMessage = itemView.findViewById(R.id.roomListItemLastMessage);
            this.onChatRoomListener = onChatRoomListener;

            itemView.setOnClickListener(this);

            chatViewModel = new ViewModelProvider((ViewModelStoreOwner) itemView.getContext()).get(ChatViewModel.class);

        }

        public void setRoom(MessageRoom room) {
            Glide.with(itemView.getContext()).load(room.getFromUser().getImageUrl()).into(userPicture);
            fromUser.setText(room.getFromUser().getName());
            lastMessage.setText(room.getAdUid() + "Siste melding: " + room.getLastMessage().getMessage());
        }

        @Override
        public void onClick(View v) {
            onChatRoomListener.onChatRoomClick(getAdapterPosition());
            MessageRoom room = chatViewModel.getRooms().getValue().get(getAdapterPosition());
            chatViewModel.getMessagesFromFirestore(room.getRoomUid());
        }
    }


    public interface OnChatRoomListener {
        void onChatRoomClick(int pos);
    }
}


