package com.example.minebratassesment.repository;

import com.example.minebratassesment.model.GitHubUser;
import com.example.minebratassesment.model.Repo;
import com.example.minebratassesment.network.GitHubApi;
import com.example.minebratassesment.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class GitHubRepository {
    private final GitHubApi api = RetrofitClient.getApi();

    public Call<GitHubUser> getUser(String username) {
        return api.getUser(username);
    }

    public Call<List<Repo>> getRepos(String username, int page, int perPage) {
        return api.getRepos(username, page, perPage);
    }
}
