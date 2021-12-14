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

import gr7.discexchange.adapter.MockAdminRecycleAdapter;
import gr7.discexchange.viewmodel.UserViewModel;


public class MockAdminFragment extends Fragment {
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private MockAdminRecycleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mock_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        recyclerView = view.findViewById(R.id.mockAdminRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        userViewModel.getUsers().observe((LifecycleOwner) view.getContext(), x -> {
            adapter = new MockAdminRecycleAdapter(view.getContext(), userViewModel.getUsers().getValue());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}