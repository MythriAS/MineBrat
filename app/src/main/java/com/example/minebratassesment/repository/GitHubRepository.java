package com.example.minebratassesment.repository;

import com.example.minebratassesment.interfaces.ResultListener;
import com.example.minebratassesment.model.GitHubUser;
import com.example.minebratassesment.model.Repo;
import com.example.minebratassesment.network.GitHubApi;
import com.example.minebratassesment.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitHubRepository {
    private final GitHubApi api = RetrofitClient.getApi();

    public void getUser(String username, ResultListener<GitHubUser> listener) {
        api.getUser(username).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("User not found.");
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                listener.onError("Error: " + t.getMessage());
            }
        });
    }

    public void getRepos(String username, int page, int perPage, ResultListener<List<Repo>> listener) {
        api.getRepos(username, page, perPage).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("No repositories found.");
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                listener.onError("Error loading repositories: " + t.getMessage());
            }
        });
    }
}

