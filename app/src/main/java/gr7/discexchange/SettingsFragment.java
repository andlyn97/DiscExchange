package gr7.discexchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean notifications = sp.getBoolean("notifications", true);
        String screenmode = sp.getString("screenmode", "systemvalgt");
        findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), "Logger ut", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        });
                return true;
            }
        });
        findPreference("mockAdmin").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Navigation.findNavController(requireActivity(), R.id.navHostFragment).navigate(R.id.notMenuMockAdmin);
                return true;
            }
        });
    }
}