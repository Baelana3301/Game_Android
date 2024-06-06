package com.example.dawnofdesolation;

import static com.google.firebase.database.core.operation.OperationSource.Source.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.Source;

public class ScoreActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.score_text_view);
        ImageButton playAgainButton = findViewById(R.id.play_again_button);
        playAgainButton.setOnClickListener(v -> {

            Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        scoreTextView.setText(String.format("Score: %d", finalScore));

        if(!MainActivity.usernameFinal.isEmpty()) {
            Log.i("MyAppTag", MainActivity.usernameFinal + " " + finalScore);
            // Создаем экземпляр DatabaseHelper
            DatabaseHelper databaseHelper = new DatabaseHelper();

            // Обновляем результат игрока
            databaseHelper.updateUserScore(MainActivity.usernameFinal, finalScore);
        }

    }

}
