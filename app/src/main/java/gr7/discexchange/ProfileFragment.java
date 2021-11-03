package gr7.discexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;


import gr7.discexchange.databinding.FragmentDetailedAdBindingImpl;
import gr7.discexchange.databinding.FragmentProfileBindingImpl;
import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.DEViewModel;

public class ProfileFragment extends Fragment {

    private DEViewModel viewModel;
    private FragmentProfileBindingImpl binding;


    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DEViewModel.class);

        RoundedImageView profilePictureImageView = view.findViewById(R.id.profilePic);

        viewModel.getUser().observe((LifecycleOwner) view.getContext(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null) {
                    return;
                }
                binding.setUser(user);

                Glide.with(view).load(user.getImageUrl()).into(profilePictureImageView);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.profileFAB);

        fab.setOnClickListener(view1 -> Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuEditProfile));

    }
}