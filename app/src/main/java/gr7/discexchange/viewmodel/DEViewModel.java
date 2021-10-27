package gr7.discexchange.viewmodel;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.R;
import gr7.discexchange.model.Ad;
import gr7.discexchange.model.User;
import gr7.discexchange.repository.IRepository;
import gr7.discexchange.repository.Repository;

public class DEViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private MutableLiveData<List<Ad>> ads;
    private List<Ad> currentAds = new ArrayList<>();
    private List<String> adsUids;


    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private Repository repository = new Repository();


    public DEViewModel() {

        if(user == null) {
            user = new MutableLiveData<>();
            user.setValue(repository.getUser());
        }

        if(ads == null) {
            ads = new MutableLiveData<>();
            currentAds.containsAll(repository.getAds());
            ads.setValue(currentAds);

        }

        if(adsUids == null) {
            adsUids = new ArrayList<>();
        }
    }



    public LiveData<List<Ad>> getAds () {
        return ads;
    }

    public void setAds(List<Ad> ad) {
        ads.setValue(ad);
    }

    public LiveData<User> getUser () {
        return user;
    }

    public void setUser(User userIn) {
        User userAfterTrim = new User();
        userAfterTrim.setName(userIn.getName().trim());
        userAfterTrim.setAddress(userIn.getAddress().trim());
        userAfterTrim.setFeedback(userIn.getFeedback());
        userAfterTrim.setStoreCredit(userIn.getStoreCredit());
        userAfterTrim.setUid(userIn.getUid().trim());
        userAfterTrim.setImageUrl(userIn.getImageUrl().trim());
        user.setValue(userAfterTrim);
    }



}
