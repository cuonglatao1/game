package com.example.ailatrieuphu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trieuphu.db";
    private static final int DATABASE_VERSION = 7;
    private static final String TAG = "DatabaseHelper";

    // Table and column definitions for questions
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_OPTION_A = "option_a";
    public static final String COLUMN_OPTION_B = "option_b";
    public static final String COLUMN_OPTION_C = "option_c";
    public static final String COLUMN_OPTION_D = "option_d";
    public static final String COLUMN_ANSWER = "answer";

    // Table and column definitions for leaderboard
    public static final String TABLE_LEADERBOARD = "leaderboard";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LEADERBOARD_TABLE = "CREATE TABLE " + TABLE_LEADERBOARD + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLAYER_NAME + " TEXT UNIQUE,"  // Đảm bảo tên người chơi là duy nhất
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CREATE_LEADERBOARD_TABLE);
        Log.d(TAG, "Leaderboard table created.");

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION + " TEXT,"
                + COLUMN_OPTION_A + " TEXT,"
                + COLUMN_OPTION_B + " TEXT,"
                + COLUMN_OPTION_C + " TEXT,"
                + COLUMN_OPTION_D + " TEXT,"
                + COLUMN_ANSWER + " TEXT)";
        db.execSQL(CREATE_QUESTIONS_TABLE);
        Log.d(TAG, "Questions table created.");

        addInitialQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEADERBOARD);
        onCreate(db);
    }

    // Method to add a player to the leaderboard with initial score
    public void addPlayerToLeaderboard(String playerName, int score) {
        if (isPlayerExists(playerName)) {
            updatePlayerScore(playerName, score); // Nếu người chơi đã tồn tại, cập nhật điểm
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYER_NAME, playerName);
            values.put(COLUMN_SCORE, score);
            long result = db.insert(TABLE_LEADERBOARD, null, values);

            if (result == -1) {
                Log.e(TAG, "Failed to insert player: " + playerName);
            } else {
                Log.d(TAG, "Player added to leaderboard: " + playerName + " with score " + score);
            }
            db.close();
        }
    }

    // Method to check if a player already exists in the leaderboard
    public boolean isPlayerExists(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LEADERBOARD,
                new String[]{COLUMN_PLAYER_NAME},
                COLUMN_PLAYER_NAME + " = ?",
                new String[]{playerName},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to update player score in the leaderboard
    public void updatePlayerScore(String playerName, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, newScore);
        int rowsAffected = db.update(TABLE_LEADERBOARD, values, COLUMN_PLAYER_NAME + " = ?", new String[]{playerName});

        if (rowsAffected == 0) {
            Log.e(TAG, "Failed to update score for player: " + playerName);
        } else {
            Log.d(TAG, "Updated score for player: " + playerName + " to " + newScore);
        }
        db.close();
    }

    // Method to retrieve leaderboard data ordered by score in descending order
    public Cursor getLeaderboard() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LEADERBOARD, null, null, null, null, null, COLUMN_SCORE + " DESC");
    }

    // Method to add a question to the database
    private void addQuestion(SQLiteDatabase db, String question, String optionA, String optionB, String optionC, String optionD, String answer) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_OPTION_A, optionA);
        values.put(COLUMN_OPTION_B, optionB);
        values.put(COLUMN_OPTION_C, optionC);
        values.put(COLUMN_OPTION_D, optionD);
        values.put(COLUMN_ANSWER, answer);
        long result = db.insert(TABLE_QUESTIONS, null, values);

        if (result == -1) {
            Log.e(TAG, "Failed to insert question: " + question);
        } else {
            Log.d(TAG, "Question added successfully: " + question);
        }
    }

    // Method to add a list of initial questions to the database
    private void addInitialQuestions(SQLiteDatabase db) {
        addQuestion(db, "Con vật nào sau đây khác với 3 con còn lại?", "Tôm", "Sò", "Nghêu", "Ngao", "Tôm");
        addQuestion(db, "Thủ đô của Pháp là gì?", "London", "Paris", "Berlin", "Madrid", "Paris");
        addQuestion(db, "2 + 2 bằng bao nhiêu?", "3", "4", "5", "6", "4");
        addQuestion(db, "Nước nào có diện tích lớn nhất thế giới?", "Mỹ", "Nga", "Trung Quốc", "Canada", "Nga");
        addQuestion(db, "Nguyên tố hóa học nào có ký hiệu là O?", "Vàng", "Oxy", "Bạc", "Sắt", "Oxy");
        addQuestion(db, "Ngôi sao nào gần Trái Đất nhất?", "Sao Hỏa", "Mặt Trời", "Sao Kim", "Sao Thủy", "Mặt Trời");
        addQuestion(db, "Quốc gia nào có dân số đông nhất?", "Ấn Độ", "Trung Quốc", "Mỹ", "Indonesia", "Trung Quốc");
        addQuestion(db, "Hành tinh nào lớn nhất trong Hệ Mặt Trời?", "Trái Đất", "Sao Hỏa", "Sao Thổ", "Sao Mộc", "Sao Mộc");
        addQuestion(db, "Động vật nào lớn nhất thế giới?", "Cá voi xanh", "Voi", "Cá mập", "Khủng long", "Cá voi xanh");
        addQuestion(db, "Quốc gia nào sản xuất cà phê nhiều nhất?", "Colombia", "Việt Nam", "Brazil", "Ethiopia", "Brazil");
        addQuestion(db, "Người Việt Nam nào đoạt giải Nobel?", "Nguyễn Du", "Lê Đức Thọ", "Phạm Tuân", "Nguyễn Ái Quốc", "Lê Đức Thọ");
        addQuestion(db, "Ai là người sáng lập Microsoft?", "Steve Jobs", "Bill Gates", "Elon Musk", "Larry Page", "Bill Gates");
        addQuestion(db, "Loại trái cây nào có hàm lượng Vitamin C cao nhất?", "Táo", "Cam", "Dứa", "Chuối", "Cam");
        addQuestion(db, "Đâu là ngôn ngữ lập trình phổ biến nhất hiện nay?", "Python", "Java", "C++", "JavaScript", "Python");
        addQuestion(db, "Cầu thủ nào được mệnh danh là 'Vua bóng đá'?", "Cristiano Ronaldo", "Lionel Messi", "Pele", "Maradona", "Pele");
        addQuestion(db, "Ai là nhà văn của tác phẩm 'Thép đã tôi thế đấy'?", "Lev Tolstoy", "Ernest Hemingway", "Nikolai Ostrovsky", "Fedor Dostoevsky", "Nikolai Ostrovsky");
        addQuestion(db, "Điện thoại di động đầu tiên được phát minh vào năm nào?", "1973", "1983", "1993", "2003", "1973");
        addQuestion(db, "Biển lớn nhất thế giới là?", "Biển Đông", "Biển Đỏ", "Biển Đen", "Biển Philippine", "Biển Philippine");
        addQuestion(db, "Tác giả của tiểu thuyết 'Chiến tranh và Hòa bình'?", "Lev Tolstoy", "Fedor Dostoevsky", "Mark Twain", "Victor Hugo", "Lev Tolstoy");
        addQuestion(db, "Bộ phận nào của cây làm nhiệm vụ hấp thụ nước?", "Lá", "Thân", "Rễ", "Hoa", "Rễ");
        addQuestion(db, "Sông nào dài nhất thế giới?", "Amazon", "Nile", "Mississippi", "Yangtze", "Nile");
        addQuestion(db, "Thể thao nào được gọi là 'King of Sports'?", "Tennis", "Bóng đá", "Bóng rổ", "Cricket", "Bóng đá");
        addQuestion(db, "Ai là nhà sáng lập của Tesla?", "Elon Musk", "Nikola Tesla", "Thomas Edison", "Henry Ford", "Elon Musk");
        addQuestion(db, "Con sông nào chảy qua thành phố London?", "Seine", "Danube", "Thames", "Rhine", "Thames");
        addQuestion(db, "Núi Everest nằm ở đâu?", "Ấn Độ", "Nepal", "Trung Quốc", "Bhutan", "Nepal");
        addQuestion(db, "Thành phố nào là thủ đô của nước Nhật Bản?", "Osaka", "Tokyo", "Kyoto", "Hiroshima", "Tokyo");
        addQuestion(db, "Loài chim nào không thể bay?", "Chim cánh cụt", "Chim sẻ", "Chim bồ câu", "Chim đại bàng", "Chim cánh cụt");
        addQuestion(db, "Đất nước nào là nơi phát minh ra Pizza?", "Pháp", "Hy Lạp", "Italia", "Mỹ", "Italia");
        addQuestion(db, "Trái Đất quay quanh mặt trời trong bao nhiêu ngày?", "30", "365", "400", "500", "365");
        addQuestion(db, "Vườn quốc gia nào lớn nhất Việt Nam?", "Phong Nha - Kẻ Bàng", "Cát Tiên", "Yok Đôn", "Cúc Phương", "Yok Đôn");

    }

    // Method to get a random question
    public Question getRandomQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " ORDER BY RANDOM() LIMIT 1", null);

        if (cursor.moveToFirst()) {
            Question question = new Question(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_A)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_B)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_C)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION_D)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER))
            );
            cursor.close();
            Log.d(TAG, "Random question loaded: " + question.getQuestionText());
            return question;
        } else {
            Log.e(TAG, "No question found in database.");
        }
        cursor.close();
        return null;
    }
}
