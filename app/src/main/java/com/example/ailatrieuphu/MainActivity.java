package com.example.ailatrieuphu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView questionText, questionNumber, totalEarnings;
    private Button answerA, answerB, answerC, answerD;
    private Button fiftyFifty, phoneFriend, askAudience, changeQuestion, stopGameButton, leaderboardButton;
    private int totalEarningsAmount = 200;  // Giả lập số tiền hiện tại
    private int currentQuestionNumber = 1;
    private String playerName;

    private DatabaseHelper databaseHelper;
    private Question currentQuestion;
    private boolean hasUsedChangeQuestion = false;
    private boolean hasUsedFiftyFifty = false;
    private boolean hasUsedPhoneFriend = false;

    private final int[] rewards = {
            200, 400, 600, 1000, 2000,
            3000, 6000, 10000, 14000, 22000,
            30000, 40000, 60000, 85000, 150000
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        playerName = getIntent().getStringExtra("playerName");
        questionText = findViewById(R.id.questionText);
        questionNumber = findViewById(R.id.questionNumber);
        totalEarnings = findViewById(R.id.totalEarnings);
        answerA = findViewById(R.id.answerA);
        answerB = findViewById(R.id.answerB);
        answerC = findViewById(R.id.answerC);
        answerD = findViewById(R.id.answerD);
        fiftyFifty = findViewById(R.id.fiftyFifty);
        phoneFriend = findViewById(R.id.phoneFriend);
        askAudience = findViewById(R.id.askAudience);
        changeQuestion = findViewById(R.id.changeQuestion);
        stopGameButton = findViewById(R.id.stopGameButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        // Khởi tạo database
        databaseHelper = new DatabaseHelper(this);

        // Thêm hoặc cập nhật điểm của người chơi trên bảng xếp hạng
        if (databaseHelper.isPlayerExists(playerName)) {
            databaseHelper.updatePlayerScore(playerName, totalEarningsAmount);
        } else {
            databaseHelper.addPlayerToLeaderboard(playerName, totalEarningsAmount);
        }

        // Tải câu hỏi đầu tiên từ cơ sở dữ liệu
        loadNewQuestion();

        // Sự kiện cho các câu trả lời
        answerA.setOnClickListener(view -> checkAnswer("A", answerA));
        answerB.setOnClickListener(view -> checkAnswer("B", answerB));
        answerC.setOnClickListener(view -> checkAnswer("C", answerC));
        answerD.setOnClickListener(view -> checkAnswer("D", answerD));

        // Sự kiện cho các bổ trợ
        fiftyFifty.setOnClickListener(v -> useFiftyFifty());
        phoneFriend.setOnClickListener(v -> phoneAFriend());
        askAudience.setOnClickListener(v -> askAudience());
        changeQuestion.setOnClickListener(v -> changeQuestion());
        stopGameButton.setOnClickListener(v -> stopGame());

        // Sự kiện mở bảng xếp hạng
        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });
    }

    // Kiểm tra câu trả lời và cập nhật điểm số
    private void checkAnswer(String selectedOption, Button selectedButton) {
        if (currentQuestion != null) {
            if (selectedButton.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
                selectedButton.setBackgroundColor(Color.GREEN); // Bôi xanh đáp án đúng
                Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();

                // Tăng tiền thưởng và cập nhật bảng xếp hạng
                totalEarningsAmount += rewards[currentQuestionNumber - 1];
                totalEarnings.setText("$" + totalEarningsAmount);
                currentQuestionNumber++;
                questionNumber.setText(currentQuestionNumber + " / 15");

                // Cập nhật điểm của người chơi trên bảng xếp hạng
                databaseHelper.updatePlayerScore(playerName, totalEarningsAmount);

                selectedButton.postDelayed(this::loadNewQuestion, 1000);
            } else {
                Toast.makeText(this, "Incorrect Answer. Game Over.", Toast.LENGTH_LONG).show();
                selectedButton.setBackgroundColor(Color.RED);
                highlightCorrectAnswer();
                endGame();
            }
        }
    }

    // Xử lý khi kết thúc trò chơi
    private void endGame() {
        Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
        intent.putExtra("totalEarnings", totalEarningsAmount);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        finish();
    }

    private void highlightCorrectAnswer() {
        if (answerA.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerA.setBackgroundColor(Color.GREEN);
        } else if (answerB.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerB.setBackgroundColor(Color.GREEN);
        } else if (answerC.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerC.setBackgroundColor(Color.GREEN);
        } else if (answerD.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerD.setBackgroundColor(Color.GREEN);
        }
    }

    private void loadNewQuestion() {
        currentQuestion = databaseHelper.getRandomQuestion();
        if (currentQuestion != null) {
            questionText.setText(currentQuestion.getQuestionText());
            answerA.setText("A. " + currentQuestion.getOptionA());
            answerB.setText("B. " + currentQuestion.getOptionB());
            answerC.setText("C. " + currentQuestion.getOptionC());
            answerD.setText("D. " + currentQuestion.getOptionD());
            resetAnswerButtonColors();
        } else {
            Log.e("MainActivity", "Failed to load question.");
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetAnswerButtonColors() {
        answerA.setBackgroundColor(Color.parseColor("#FF6200EE"));
        answerB.setBackgroundColor(Color.parseColor("#FF6200EE"));
        answerC.setBackgroundColor(Color.parseColor("#FF6200EE"));
        answerD.setBackgroundColor(Color.parseColor("#FF6200EE"));
    }

    private void useFiftyFifty() {
        if (hasUsedFiftyFifty) {
            Toast.makeText(this, "You have already used 50:50.", Toast.LENGTH_SHORT).show();
            return;
        }
        hasUsedFiftyFifty = true;
        Toast.makeText(this, "50:50 used", Toast.LENGTH_SHORT).show();
        if (!answerA.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerA.setText("");
        }
        if (!answerB.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerB.setText("");
        }
        if (!answerC.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerC.setText("");
        }
        if (!answerD.getText().toString().endsWith(currentQuestion.getCorrectAnswer())) {
            answerD.setText("");
        }
    }

    private void phoneAFriend() {
        if (hasUsedPhoneFriend) {
            Toast.makeText(this, "You have already used the Phone a Friend option.", Toast.LENGTH_SHORT).show();
            return;
        }
        hasUsedPhoneFriend = true;
        if (currentQuestion != null) {
            PhoneFriendDialog phoneFriendDialog = new PhoneFriendDialog(this, currentQuestion.getCorrectAnswer());
            phoneFriendDialog.show();
        } else {
            Toast.makeText(this, "No question available for help.", Toast.LENGTH_SHORT).show();
        }
    }

    private void askAudience() {
        Toast.makeText(this, "Audience suggests: " + currentQuestion.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
    }

    private void changeQuestion() {
        if (!hasUsedChangeQuestion) {
            hasUsedChangeQuestion = true;
            Toast.makeText(this, "Question changed!", Toast.LENGTH_SHORT).show();
            loadNewQuestion();
        } else {
            Toast.makeText(this, "You have already used the Change Question option.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopGame() {
        endGame();
    }
}
