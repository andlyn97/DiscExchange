package gr7.discexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.MessageRoom;

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

        private TextView roomTV;
        private OnChatRoomListener onChatRoomListener;

        public ChatViewHolder(@NonNull View itemView, OnChatRoomListener onChatRoomListener) {
            super(itemView);

            roomTV = itemView.findViewById(R.id.roomRVItem);
            this.onChatRoomListener = onChatRoomListener;

            itemView.setOnClickListener(this);

        }

        public void setRoom(MessageRoom room) {

            roomTV.setText("User: " + room.getFromUser().getName() + "Siste melding: " + room.getLastMessage().getMessage());

        }

        @Override
        public void onClick(View v) {
            onChatRoomListener.onChatRoomClick(getAdapterPosition());
        }
    }


    public interface OnChatRoomListener {
        void onChatRoomClick(int pos);
    }
}


