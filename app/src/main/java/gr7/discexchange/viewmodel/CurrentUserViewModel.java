package gr7.discexchange.viewmodel;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import gr7.discexchange.R;
import gr7.discexchange.model.User;

public class CurrentUserViewModel extends ViewModel {
    private MutableLiveData<User> user;


    public CurrentUserViewModel() {
        if(user != null) {return;}
        user = new MutableLiveData<>();
        loadUser();
    }

    private void loadUser() {
        String loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore
                .collection("user")
                .whereEqualTo("uid", loggedInUserUid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().size() == 1) {
                        DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);
                        User fetchedUser = userDoc.toObject(User.class);
                        user.setValue(fetchedUser);
                        Log.d("Debug12", "UserIid: " + user.getValue().toString() + " Name: " + user.getValue().getName());
                    }
                });


    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}
