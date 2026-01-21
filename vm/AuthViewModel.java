package com.utaste.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.utaste.data.model.User;
import com.utaste.data.repo.AppRepository;

public class AuthViewModel extends ViewModel {
    private final AppRepository repo;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public AuthViewModel(AppRepository repo) { this.repo = repo; }
    public LiveData<User> user() { return currentUser; }

    public boolean login(String email, String pwd) {
        User u = repo.authenticate(email, pwd);
        currentUser.postValue(u);
        return u != null;
    }
}
