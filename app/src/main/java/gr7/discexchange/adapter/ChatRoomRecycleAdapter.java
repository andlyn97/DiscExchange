package gr7.discexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        if(message.getFromUserUid().equals(FirebaseAuth.getInstance().getUid())) {
            holder.itemView.setBackgroundResource(R.drawable.sender_chat_style);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.receiver_chat_style);
        }

        holder.setMessage(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{

        private TextView sentFromTV = itemView.findViewById(R.id.messageSentFrom);
        private TextView messageTV = itemView.findViewById(R.id.message);
        private TextView dateTV = itemView.findViewById(R.id.messageDate);

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setMessage(Message message) {
            sentFromTV.setText(message.getFromUser().getName());
            messageTV.setText(message.getMessage());

            LocalDateTime sentAt = Instant.ofEpochMilli(Long.parseLong(message.getSentAt())).atZone(ZoneId.systemDefault()).toLocalDateTime(); // Stole some code from here: https://www.candidjava.com/date-time/how-to-convert-epoch-time-to-date-in-java/
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formatedSentAt = sentAt.format(dateTimeFormatter);
            dateTV.setText(formatedSentAt);

        }
    }
}
