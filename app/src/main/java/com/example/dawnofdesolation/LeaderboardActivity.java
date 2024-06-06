package com.example.dawnofdesolation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView для отображения лидерборда.


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.fetchAllUsersScore(new DatabaseHelper.UsersCallback() {
            @Override
            public void onUsersFetched(ArrayList<DatabaseHelper.Users> users) {
                LeaderboardAdapter adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.w("StatsActivity", "Error fetching users: " + error.toException());
            }
        });


    }

    public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

        private ArrayList<DatabaseHelper.Users> leaderboard;

        public LeaderboardAdapter(ArrayList<DatabaseHelper.Users> leaderboard) {
            this.leaderboard = leaderboard;
            Collections.reverse(leaderboard);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DatabaseHelper.Users user = leaderboard.get(position);
            holder.usernameTextView.setText(user.username);
            holder.scoreTextView.setText(String.valueOf(user.score));

        }

        @Override
        public int getItemCount() {
            return leaderboard.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView usernameTextView;
            public TextView scoreTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                usernameTextView = itemView.findViewById(R.id.usernameTextView);
                scoreTextView = itemView.findViewById(R.id.scoreTextView);
            }
        }
    }


}
