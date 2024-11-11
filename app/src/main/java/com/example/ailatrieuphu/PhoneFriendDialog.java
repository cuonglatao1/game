package com.example.ailatrieuphu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

public class PhoneFriendDialog extends Dialog {

    private String correctAnswer;  // Đáp án đúng được truyền từ MainActivity

    public PhoneFriendDialog(Context context, String correctAnswer) {
        super(context);
        this.correctAnswer = correctAnswer;  // Gán đáp án đúng vào trường này
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phone_friend);

        // Sử dụng ID chính xác cho các thành phần giao diện
        TextView friendAdviceText = findViewById(R.id.friendAdviceText);  // TextView để hiển thị gợi ý từ người thân
        TextView friendAnswer = findViewById(R.id.friendAnswer);  // TextView để hiển thị đáp án
        Button thankYouButton = findViewById(R.id.thankYouButton);  // Nút cảm ơn để đóng dialog

        // Hiển thị đáp án đúng từ người thân
        friendAdviceText.setText("Tôi chắc chắn đáp án chính xác cuối cùng là đáp án:");
        friendAnswer.setText(correctAnswer);  // Gán đáp án đúng vào friendAnswer

        // Đóng dialog khi nhấn nút "Cảm ơn"
        thankYouButton.setOnClickListener(v -> dismiss());
    }
}
