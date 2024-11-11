package com.example.ailatrieuphu;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLeaderboard;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize the RecyclerView
        recyclerViewLeaderboard = findViewById(R.id.recyclerViewLeaderboard);
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve and add player details if available
        String playerName = getIntent().getStringExtra("playerName");
        int totalEarnings = getIntent().getIntExtra("totalEarnings", 0);
        if (playerName != null && totalEarnings > 0) {
            addOrUpdatePlayerInLeaderboard(playerName, totalEarnings);
        }

        // Load the leaderboard data and set up the adapter
        List<Player> playerList = loadLeaderboardData();
        LeaderboardAdapter adapter = new LeaderboardAdapter(this, playerList);
        recyclerViewLeaderboard.setAdapter(adapter);
        // Set up the back button to finish the activity
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private List<Player> loadLeaderboardData() {
        List<Player> players = new ArrayList<>();
        Cursor cursor = databaseHelper.getLeaderboard();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAYER_NAME));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE));
                players.add(new Player(name, score));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return players;
    }

    private void addOrUpdatePlayerInLeaderboard(String playerName, int score) {
        Cursor cursor = databaseHelper.getLeaderboard(); // Check if player exists in the leaderboard

        boolean playerExists = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAYER_NAME));
                if (name.equals(playerName)) {
                    playerExists = true;
                    break;
                }
            }
            cursor.close();
        }

        if (playerExists) {
            // Update player score if already exists
            databaseHelper.updatePlayerScore(playerName, score);
        } else {
            // Add player to leaderboard if not found
            databaseHelper.addPlayerToLeaderboard(playerName, score);
        }
    }
}
