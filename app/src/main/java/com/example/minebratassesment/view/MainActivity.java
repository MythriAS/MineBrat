package com.example.minebratassesment.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.minebratassesment.R;
import com.example.minebratassesment.adapter.RepoAdapter;
import com.example.minebratassesment.viewmodel.GitHubViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GitHubViewModel viewModel;
    private RepoAdapter adapter;
    private EditText usernameInput;
    private ImageView avatar;
    private TextView name, bio, repoCount, error;
    private RecyclerView recyclerView;
    private LinearLayout lnrSearch;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.username_input);
        searchBtn = findViewById(R.id.search_btn);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.bio);
        repoCount = findViewById(R.id.repo_count);
        error = findViewById(R.id.error);
        recyclerView = findViewById(R.id.recycler);
        lnrSearch = findViewById(R.id.lnr_search);


        viewModel = new ViewModelProvider(this).get(GitHubViewModel.class);

        adapter = new RepoAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (!rv.canScrollVertically(1)) viewModel.loadRepos();
            }
        });

        searchBtn.setOnClickListener(v -> {
            String input = usernameInput.getText().toString().trim();
            String extractedUsername = extractUsernameFromInput(input);

            if (!TextUtils.isEmpty(extractedUsername)) {
                viewModel.fetchUser(extractedUsername);
                error.setVisibility(GONE);
                lnrSearch.setVisibility(VISIBLE);
            } else {
                error.setText("Please enter a valid GitHub username or profile URL.");
                error.setVisibility(VISIBLE);
            }
        });

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, u -> {
            name.setText("Name: " + (u.name != null ? u.name : "N/A"));
            bio.setText("Bio: " + (u.bio != null ? u.bio : "N/A"));
            repoCount.setText("Repos: " + u.public_repos);
            Glide.with(this).load(u.avatar_url).into(avatar);
        });

        viewModel.getRepos().observe(this, repos -> adapter.setRepoList(repos));

        viewModel.getError().observe(this, msg -> {
            if (msg != null) {
                error.setText(msg);
                error.setVisibility(VISIBLE);
            } else {
                error.setVisibility(GONE);
            }
        });
    }

    private String extractUsernameFromInput(String input) {
        input = input.trim();
        if (input.startsWith("http://github.com/") || input.startsWith("https://github.com/")) {
            Uri uri = Uri.parse(input);
            String username = uri.getLastPathSegment();

            if (username != null && username.contains("?")) {
                username = username.split("\\?")[0];
            }
            return TextUtils.isEmpty(username) ? null : username;
        } else {
            return input;
        }
    }

}