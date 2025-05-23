package com.example.minebratassesment.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.minebratassesment.interfaces.ResultListener;
import com.example.minebratassesment.model.GitHubUser;
import com.example.minebratassesment.model.Repo;
import com.example.minebratassesment.repository.GitHubRepository;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitHubViewModel extends ViewModel {

    private final GitHubRepository repository = new GitHubRepository();

    private final MutableLiveData<GitHubUser> user = new MutableLiveData<>();
    private final MutableLiveData<List<Repo>> repos = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private int currentPage = 1;
    private String currentUsername = "";

    public LiveData<GitHubUser> getUser() { return user; }
    public LiveData<List<Repo>> getRepos() { return repos; }

    public LiveData<String> getError() { return error; }

    public void fetchUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            error.setValue("Invalid username.");
            return;
        }

        loading.setValue(true);
        currentUsername = username;
        currentPage = 1;

        repository.getUser(username, new ResultListener<GitHubUser>() {
            @Override
            public void onSuccess(GitHubUser data) {
                user.setValue(data);
                repos.setValue(new ArrayList<>());
                loadRepos();
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void loadRepos() {
        loading.setValue(true);
        repository.getRepos(currentUsername, currentPage, 10, new ResultListener<List<Repo>>() {
            @Override
            public void onSuccess(List<Repo> data) {
                List<Repo> currentList = repos.getValue();
                if (currentList != null) {
                    currentList.addAll(data);
                    repos.setValue(currentList);
                    currentPage++;
                }
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }
}