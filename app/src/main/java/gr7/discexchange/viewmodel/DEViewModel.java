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
    private FirebaseFirestore fireStore;
    private Repository repository;
    private MutableLiveData<User> user;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> userAds;


    public DEViewModel() {
        fireStore = FirebaseFirestore.getInstance();
        repository = new Repository();
        ads = new MutableLiveData<>();
        userAds = new MutableLiveData<>();
        user = repository.getUser();
        ads = repository.getAds();
        userAds = repository.getUserAds(true);
    }

    public MutableLiveData<List<Ad>> getAds () {
        return ads;
    }

    public void setAds(List<Ad> ads) {

    }

    public MutableLiveData<User> getUser () {
        return user;
    }

    public void setUser(User user) {

    }

    public MutableLiveData<List<Ad>> getUserAds(boolean isArchived) {
        userAds = repository.getUserAds(isArchived);
        return userAds;
    }

    public MutableLiveData<List<Ad>> getUserAds() {
        return userAds;
    }

    public void setUserAds(MutableLiveData<List<Ad>> userAds) {
        this.userAds = userAds;
    }
}
