package com.example.ailatrieuphu;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnterNameActivity extends AppCompatActivity {

    private EditText playerNameInput;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        // Ánh xạ `playerNameInput` từ XML
        playerNameInput = findViewById(R.id.playerNameInput);

        // Giới hạn tên người chơi tối đa 10 ký tự
        playerNameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        // Ánh xạ `startGameButton` từ XML
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString();
            if (!playerName.isEmpty()) {  // Kiểm tra nếu tên không rỗng
                Intent intent = new Intent(EnterNameActivity.this, MainActivity.class);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên của bạn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
