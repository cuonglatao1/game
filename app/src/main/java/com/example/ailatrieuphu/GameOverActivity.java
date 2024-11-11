package com.example.ailatrieuphu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Nhận thông tin điểm số và tên người chơi từ Intent
        Intent intent = getIntent();
        int totalEarnings = intent.getIntExtra("totalEarnings", 0);
        String playerName = intent.getStringExtra("playerName");

        // Thêm người chơi vào bảng xếp hạng
        DatabaseHelper db = new DatabaseHelper(this);
        db.addPlayerToLeaderboard(playerName, totalEarnings);

        // Hiển thị điểm số
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Bạn sẽ ra về với phần thưởng: $" + totalEarnings);

        // Nút thoát và quay lại màn hình nhập tên
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            Intent backToEnterNameIntent = new Intent(GameOverActivity.this, EnterNameActivity.class);
            backToEnterNameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backToEnterNameIntent);
            finish();
        });
    }
}
