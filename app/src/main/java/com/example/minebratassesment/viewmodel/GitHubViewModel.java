package com.example.minebratassesment.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void fetchUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            error.setValue("Invalid username.");
            return;
        }

        loading.setValue(true);
        currentUsername = username;
        currentPage = 1;

        repository.getUser(username).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    user.setValue(response.body());
                    repos.setValue(new ArrayList<>());
                    loadRepos();
                } else {
                    error.setValue("User not found.");
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                loading.setValue(false);
                error.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void loadRepos() {
        loading.setValue(true);
        repository.getRepos(currentUsername, currentPage, 10).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Repo> currentList = repos.getValue();
                    currentList.addAll(response.body());
                    repos.setValue(currentList);
                    currentPage++;
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}
