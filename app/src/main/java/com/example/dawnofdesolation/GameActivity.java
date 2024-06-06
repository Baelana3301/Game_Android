package com.example.dawnofdesolation;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity {

    private final Entity player = new Entity(0,0,0);
    private static final List<Entity> enemies = new ArrayList<>();

    private Drawable player_back;
    private Drawable empty_back;
    private Drawable enemy_back;
    private Drawable dead_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        player_back = ContextCompat.getDrawable(this, R.drawable.cage_player);
        empty_back = ContextCompat.getDrawable(this, R.drawable.cage_empty);
        enemy_back = ContextCompat.getDrawable(this, R.drawable.cage_enemy);
        dead_back = ContextCompat.getDrawable(this, R.drawable.cage_enemy_dead);
        Button[][] gameBoard = Mechanics.generateGameBoard(this);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        initializeBoard(gridLayout, gameBoard);
    }



    private void initializeBoard(GridLayout gridLayout, Button[][] gameBoard) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                gameBoard[row][col] = getBoardCell(row, col, gameBoard);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0);

                gridLayout.addView(gameBoard[row][col], params);
            }
        }

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                int finalRow = row;
                int finalCol = col;
                gameBoard[row][col].setOnClickListener(v -> boardCellClick(gameBoard, finalRow, finalCol));
            }
        }

    }

    private void boardCellClick(Button[][] gameBoard, int row, int col) {
        Log.e("aaaa", String.valueOf(gameBoard[row][col].getText()));
        if(gameBoard[row][col].getText() == "0") {
            if (player.actions > 0 && Math.abs(player.row - row) <= 1 && Math.abs(player.col - col) <= 1) {
                Button prevCell = findViewById(player.id);
                prevCell.setBackground(empty_back);
                gameBoard[row][col].setBackground(player_back);
                gameBoard[player.row][player.col].setText("0");
                player.row = row;
                player.col = col;
                gameBoard[player.row][player.col].setText("P");
                player.id = gameBoard[row][col].getId();
                --player.actions;
            }
        }
        if(gameBoard[row][col].getText() == "E") {
            for(int i = 0; i < enemies.size(); ++i) {
                if(gameBoard[row][col].getId() == enemies.get(i).id) {
                    Mechanics.playerAttack(player, enemies.get(i));
                    if(enemies.get(i).health <= 0) {
                        Button enemy = findViewById(enemies.get(i).id);
                        enemy.setBackground(dead_back);
                        gameBoard[row][col].setText("D");
                    }
                    break;
                }
            }
        }
    }

    public static void enemyWalk(Entity enemy, Entity player) {
        if (enemy.actions > 0) {
            if(enemy.row == player.row && enemy.col < player.col) {

            }
        }
    }

    private Button getBoardCell(int row, int col, Button[][] gameBoard) {
        Button boardCell = gameBoard[row][col];
        if(boardCell.getText() == "1") {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.wallCell), null, 0);
            boardCell.setText("1");
            boardCell.setId(View.generateViewId());
            return boardCell;
        }
        if(boardCell.getText() == "E") {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.enemyCell), null, 0);
            boardCell.setId(View.generateViewId());
            boardCell.setText("E");
            Entity enemy = new Entity(row, col, boardCell.getId());
            enemies.add(enemy);
            return boardCell;
        }
        if(boardCell.getText() == "P") {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.playerCell), null, 0);
            boardCell.setId(View.generateViewId());
            boardCell.setText("P");
            player.row = row;
            player.col = col;
            player.id = boardCell.getId();
            return boardCell;
        }
        boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.emptyCell), null, 0);
        boardCell.setText("0");
        boardCell.setId(View.generateViewId());
        return boardCell;
    }
}
