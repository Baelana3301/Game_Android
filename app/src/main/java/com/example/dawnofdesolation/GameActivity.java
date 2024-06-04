package com.example.dawnofdesolation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        char[][] gameBoard = Mechanics.generateGameBoard();
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        initializeBoard(gridLayout, gameBoard);
    }



    private void initializeBoard(GridLayout gridLayout, char[][] gameBoard) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                Button boardCell = getBoardCell(row, col, gameBoard);
                boardCell.setId(View.generateViewId());
                int finalRow = row;
                int finalCol = col;
                boardCell.setOnClickListener(v -> boardCellClick(boardCell, gameBoard, finalRow, finalCol));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0);

                gridLayout.addView(boardCell, params);
            }
        }
    }

    private void boardCellClick(Button boardCell, char[][] gameBoard, int row, int col) {

    }

    private Button getBoardCell(int row, int col, char[][] gameBoard) {
        Button boardCell;
        if(gameBoard[row][col] == '1') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.wallCell), null, 0);
            return boardCell;
        }
        if(gameBoard[row][col] == 'E') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.enemyCell), null, 0);
            return boardCell;
        }
        if(gameBoard[row][col] == 'D') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.deadCell), null, 0);
            return boardCell;
        }
        if(gameBoard[row][col] == 'P') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.playerCell), null, 0);
            return boardCell;
        }
        return new Button(new android.view.ContextThemeWrapper(this, R.style.emptyCell), null, 0);
    }
}
