package gr7.discexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.MessageRoom;
import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.ChatViewModel;

public class MockAdminRecycleAdapter extends RecyclerView.Adapter<MockAdminRecycleAdapter.MockAdminViewHolder> {
    private LayoutInflater inflater;
    private List<User> users;


    public MockAdminRecycleAdapter(Context context, List<User> users) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public MockAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.mock_admin_list_item, parent, false);

        return new MockAdminViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MockAdminViewHolder holder, int position) {
        User user = users.get(position);
        holder.setUser(user);
    }

    @Override
    public int getItemCount() {
        if(users == null) {
            return 0;
        }
        return users.size();
    }



    public class MockAdminViewHolder extends RecyclerView.ViewHolder {

        private AppCompatButton button;
        private TextView name;
        private TextInputLayout storecredit;
        private AppCompatRatingBar rating;


        public MockAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.mockAdminUpdateButton);
            name = itemView.findViewById(R.id.mockAdminItemName);
            storecredit = itemView.findViewById(R.id.mockAdminItemStorecredit);
            rating = itemView.findViewById(R.id.mockAdminItemRating);

        }

        public void setUser(User user) {
            name.setText(user.getName());
            storecredit.getEditText().setText(String.valueOf(user.getStoreCredit()));
            rating.setRating(user.getFeedback());
        }

    }


}


