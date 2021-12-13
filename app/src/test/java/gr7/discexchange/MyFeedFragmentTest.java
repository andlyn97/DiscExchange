package gr7.discexchange;

import static org.junit.Assert.*;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;

import org.junit.Test;

public class MyFeedFragmentTest {
    @IdRes
    private final int THEME = R.style.Theme_DiscExchange_NoActionBar;

    @Test
    public void onViewCreated() {
    }

    @Test
    public void onCardClick() {
        Bundle bundle = new Bundle();
        FragmentScenario<MyFeedFragment> scenario = FragmentScenario.launchInContainer(MyFeedFragment.class, bundle, THEME,  Lifecycle.State.STARTED);

        scenario.onFragment(fragment -> {
           assertNotNull(fragment.getActivity());
           TestNavHostController navController = new TestNavHostController(fragment.getActivity());
           fragment.getActivity().runOnUiThread(() -> navController.setGraph(R.navigation.main));
            Navigation.setViewNavController(fragment.requireView(), navController);

            navController.navigate(R.id.notMenuDetailedAd);
            NavDestination destination = navController.getCurrentDestination();
            assertNotNull(destination);
            assertEquals(destination.getId(), R.id.notMenuDetailedAd);
        });

    }
    // https://stackoverflow.com/questions/51229277/how-to-write-tests-for-android-navigation-controller
}