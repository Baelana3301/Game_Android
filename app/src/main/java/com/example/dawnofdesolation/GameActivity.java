package com.example.dawnofdesolation;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
        if(gameBoard[row][col].getText() == "D") {
            if (player.actions > 0 && Math.abs(player.row - row) <= 1 && Math.abs(player.col - col) <= 1) {
                gameBoard[row][col].setBackground(empty_back);
                gameBoard[row][col].setText("0");
                --player.actions;
            }
        }
        if(gameBoard[row][col].getText() == "E" && player.actions > 0) {
            for(int i = 0; i < enemies.size(); ++i) {
                if(gameBoard[row][col].getId() == enemies.get(i).id) {
                    Mechanics.playerAttack(player, enemies.get(i));
                    if(enemies.get(i).health <= 0) {
                        gameBoard[row][col].setBackground(dead_back);
                        gameBoard[row][col].setText("D");
                        //enemies.
                    }
                    break;
                }
            }
        }
        if(player.actions == 0) {

        }

    }

    public void enemyWalk(Entity enemy, Entity player, Button[][] gameBoard) {
        if (enemy.actions > 0) {
            if(enemy.row == player.row && enemy.col < player.col && gameBoard[enemy.row][enemy.col + 1].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row][enemy.col + 1].setBackground(enemy_back);
                gameBoard[enemy.row][enemy.col + 1].setText("E");
                --enemy.actions;
            }
            else if(enemy.row == player.row && enemy.col > player.col && gameBoard[enemy.row][enemy.col - 1].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row][enemy.col - 1].setBackground(enemy_back);
                gameBoard[enemy.row][enemy.col - 1].setText("E");
                --enemy.actions;
            }
            else if(enemy.row < player.row && enemy.col == player.col && gameBoard[enemy.row + 1][enemy.col].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row + 1][enemy.col].setBackground(enemy_back);
                gameBoard[enemy.row + 1][enemy.col].setText("E");
                --enemy.actions;
            }
            else if(enemy.row > player.row && enemy.col == player.col && gameBoard[enemy.row - 1][enemy.col].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row - 1][enemy.col].setBackground(enemy_back);
                gameBoard[enemy.row - 1][enemy.col].setText("E");
                --enemy.actions;
            }
            else {
                Random random = new Random();
                int randomRow = random.nextInt(3) - 1; // Генерирует случайное число от -1 до 1
                int randomCol = random.nextInt(3) - 1;
                if (!(enemy.row + randomRow < 8 && enemy.row + randomRow >= 0)) {
                    randomRow = 0;
                }
                if (!(enemy.col + randomRow < 8 && enemy.col + randomRow >= 0)) {
                    randomCol = 0;
                }
                if(gameBoard[enemy.row + randomRow][enemy.col + randomCol].getText() == "0") {
                    gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                    gameBoard[enemy.row][enemy.col].setText("0");
                    gameBoard[enemy.row + randomRow][enemy.col + randomCol].setBackground(enemy_back);
                    gameBoard[enemy.row + randomRow][enemy.col + randomCol].setText("E");
                    --enemy.actions;

                }
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
