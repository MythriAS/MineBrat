package com.example.minebratassesment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minebratassesment.model.Repo;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private List<Repo> repoList;

    public RepoAdapter(List<Repo> repos) {
        this.repoList = repos;
    }

    public void setRepoList(List<Repo> newList) {
        this.repoList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.name.setText(repoList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    static class RepoViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(android.R.id.text1);
        }
    }
}
