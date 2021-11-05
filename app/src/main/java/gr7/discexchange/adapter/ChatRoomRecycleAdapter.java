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
import gr7.discexchange.model.Message;

public class ChatRoomRecycleAdapter extends RecyclerView.Adapter<ChatRoomRecycleAdapter.ChatRoomViewHolder> {

    private LayoutInflater inflater;
    private List<Message> messages;

    public ChatRoomRecycleAdapter(Context context, List<Message> messages) {
        this.inflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.message_list_item, parent, false);

        return new ChatRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.setMessage(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{

        private TextView sentFromTV = itemView.findViewById(R.id.messageSentFrom);
        private TextView messageTV = itemView.findViewById(R.id.message);

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setMessage(Message message) {
            sentFromTV.setText("From: " + message.getFromUserUid());
            messageTV.setText("Message: " + message.getMessage());

        }
    }
}
