package com.example.ailatrieuphu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final List<Player> players;
    private final Context context;

    public LeaderboardAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.nameTextView.setText(player.getName());
        holder.scoreTextView.setText(String.valueOf(player.getScore()));

        // Reset any previous styling
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        holder.nameTextView.setTypeface(Typeface.DEFAULT);
        holder.scoreTextView.setTypeface(Typeface.DEFAULT);
        holder.trophyIcon.setVisibility(View.GONE);

        // Customize top 3 players with bold and colored text
        if (position == 0) { // First place
            holder.nameTextView.setTextColor(Color.parseColor("#D4AF37"));
            holder.nameTextView.setTypeface(holder.nameTextView.getTypeface(), Typeface.BOLD);
            holder.scoreTextView.setTypeface(holder.scoreTextView.getTypeface(), Typeface.BOLD);
            holder.trophyIcon.setVisibility(View.VISIBLE);
            holder.trophyIcon.setImageResource(R.drawable.gold_cup); // Set gold trophy icon
        } else if (position == 1) { // Second place
            holder.nameTextView.setTextColor(Color.parseColor("#A9A9A9"));
            holder.nameTextView.setTypeface(holder.nameTextView.getTypeface(), Typeface.BOLD);
            holder.scoreTextView.setTypeface(holder.scoreTextView.getTypeface(), Typeface.BOLD);
            holder.trophyIcon.setVisibility(View.VISIBLE);
            holder.trophyIcon.setImageResource(R.drawable.silver_cup); // Set silver trophy icon
        } else if (position == 2) { // Third place
            holder.nameTextView.setTextColor(Color.parseColor("#CD7F32"));
            holder.nameTextView.setTypeface(holder.nameTextView.getTypeface(), Typeface.BOLD);
            holder.scoreTextView.setTypeface(holder.scoreTextView.getTypeface(), Typeface.BOLD);
            holder.trophyIcon.setVisibility(View.VISIBLE);
            holder.trophyIcon.setImageResource(R.drawable.bronze_cup); // Set bronze trophy icon
        } else {
            // Regular color for other players
            holder.nameTextView.setTextColor(Color.WHITE); // Default text color
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView scoreTextView;
        ImageView trophyIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.playerName);
            scoreTextView = itemView.findViewById(R.id.playerScore);
            trophyIcon = itemView.findViewById(R.id.trophyIcon);
        }
    }
}
