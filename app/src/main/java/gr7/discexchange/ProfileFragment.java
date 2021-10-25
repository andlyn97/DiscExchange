package gr7.discexchange;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.CurrentUserViewModel;

public class ProfileFragment extends Fragment {

    MainActivity mainActivity;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        RoundedImageView pictureTextView = view.findViewById(R.id.profilePic);
        TextView nameTextView = view.findViewById(R.id.profileName);
        TextView addressTextView = view.findViewById(R.id.profileAddress);
        RatingBar ratingBar= view.findViewById(R.id.profileUserRatingBar);
        TextView storeCreditTextView = view.findViewById(R.id.profileStoreCredit);

        mainActivity.currentUserViewModel.getUser().observe((LifecycleOwner) view.getContext(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameTextView.setText(user.getName());
                addressTextView.setText(user.getAddress());
                ratingBar.setRating(user.getFeedback());
                storeCreditTextView.setText("Butikk kredit: " + user.getStoreCredit());
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.profileFAB);

        fab.setOnClickListener(view1 -> Navigation.findNavController(mainActivity, R.id.navHostFragment).navigate(R.id.notMenuEditProfile));






    }
}