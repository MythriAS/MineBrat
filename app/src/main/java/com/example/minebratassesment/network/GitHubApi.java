package com.example.minebratassesment.network;

import com.example.minebratassesment.model.GitHubUser;
import com.example.minebratassesment.model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApi {
    @GET("users/{username}")
    Call<GitHubUser> getUser(@Path("username") String username);

    @GET("users/{username}/repos")
    Call<List<Repo>> getRepos(@Path("username") String username,
                              @Query("page") int page,
                              @Query("per_page") int perPage);
}
