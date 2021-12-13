package gr7.discexchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.firebase.ui.auth.AuthUI;

import gr7.discexchange.service.ChatForegroundService;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = SettingsFragment.class.getName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());



        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            findPreference("screenmode").setVisible(false);
        }
        // Dark mode
        findPreference("screenmode").setOnPreferenceChangeListener((preference, newValue) -> {
            sp.edit().putString("screenmode", newValue.toString()).apply();
            getActivity().finish();
            Intent intent = getActivity().getIntent();
            getActivity().startActivity(intent);
            return true;
        });

        // Mock admin
        findPreference("mockAdmin").setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuMockAdmin);
            return true;
        });

        findPreference("storeCreate").setOnPreferenceClickListener(preference -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "Settings");
            Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuCreateAd, bundle);
            return true;
        });

        // Logg ut
        findPreference("logout").setOnPreferenceClickListener(preference -> {
            AuthUI.getInstance()
                    .signOut(getContext())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getContext(), "Logger ut", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    });
            return true;
        });
    }
}