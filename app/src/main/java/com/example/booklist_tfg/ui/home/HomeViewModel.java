package com.example.booklist_tfg.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        ///mText.setValue("This is home fragment");
        mText.setValue("assssuuuun");
    }

    public LiveData<String> getText() {
        return mText;
    }
}