package com.example.dawnofdesolation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DawnPrefs";
    private static final String NICKNAME_KEY = "nickname";
    private EditText editTextNickname;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editTextNickname = findViewById(R.id.edittext_nickname);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadNickname();
        editTextNickname.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNickname();
                closeKeyboardAndClearFocus(editTextNickname);
                return true;
            }
            return false;
        });
    }

    private void saveNickname() {
        String nickname = editTextNickname.getText().toString().trim();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NICKNAME_KEY, nickname);
        editor.apply();
    }

    private void loadNickname() {
        String nickname = prefs.getString(NICKNAME_KEY, "");
        editTextNickname.setText(nickname);
    }

    private void closeKeyboardAndClearFocus(EditText editText) {
        // Скрытие клавиатуры и удаление фокуса
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }
}