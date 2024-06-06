package com.example.dawnofdesolation;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameActivity extends AppCompatActivity {

    private final Entity player = new Entity(0,0,0);
    private  final List<Entity> enemies = new ArrayList<>();

    private Drawable player_back;
    private Drawable empty_back;
    private Drawable enemy_back;
    private Drawable dead_back;
    private Drawable player_dead;
    public static int score = 0;
    private TextView scoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        player_back = ContextCompat.getDrawable(this, R.drawable.cage_player);
        empty_back = ContextCompat.getDrawable(this, R.drawable.cage_empty);
        enemy_back = ContextCompat.getDrawable(this, R.drawable.cage_enemy);
        dead_back = ContextCompat.getDrawable(this, R.drawable.cage_enemy_dead);
        player_dead = ContextCompat.getDrawable(this, R.drawable.cage_player_dead);
        scoreView = findViewById(R.id.scoreView);
        Button[][] gameBoard = Mechanics.generateGameBoard(this);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        initializeBoard(gridLayout, gameBoard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        score = 0;

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
        Log.e("aaaa", String.valueOf(player.actions));
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
                score += 100;
                scoreView.setText("SCORE " + score);
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
                        enemies.remove(i);
                        score += 50;
                        scoreView.setText("SCORE " + score);
                    }
                    break;
                }
            }
        }
        if(player.actions == 0) {
            for(int i = 0; i < enemies.size(); ++i) {
                Mechanics.enemyAttack(player, enemies.get(i));
                if(player.health == 0) {
                    gameBoard[player.row][player.col].setBackground(player_dead);
                    TextView gameover = findViewById(R.id.gameover);
                    TextView tap = findViewById(R.id.tap);
                    gameover.setVisibility(View.VISIBLE);
                    tap.setVisibility(View.VISIBLE);
                    tap.setOnClickListener(v -> {
                        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                        startActivity(intent);
                    });
                }
                enemyWalk(enemies.get(i), player, gameBoard);
            }
            spawnEnemy(gameBoard);
            player.actions = 2;
        }

    }

    public void spawnEnemy(Button[][] gameBoard) {
        Random random = new Random();
        int randomRow = random.nextInt(10);
        int randomCol = random.nextInt(8);
        if(gameBoard[randomRow][randomCol].getText() == "0") {
            gameBoard[randomRow][randomCol].setBackground(enemy_back);
            gameBoard[randomRow][randomCol].setText("E");
            Entity enemy = new Entity(randomRow, randomCol, gameBoard[randomRow][randomCol].getId());
            enemies.add(enemy);
        }
    }



    public void enemyWalk(Entity enemy, Entity player, Button[][] gameBoard) {
        if (enemy.actions > 0) {
            if(enemy.row == player.row && enemy.col < player.col && gameBoard[enemy.row][enemy.col + 1].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row][enemy.col + 1].setBackground(enemy_back);
                gameBoard[enemy.row][enemy.col + 1].setText("E");
                enemy.col++;
                enemy.id = gameBoard[enemy.row][enemy.col].getId();
            }
            else if(enemy.row == player.row && enemy.col > player.col && gameBoard[enemy.row][enemy.col - 1].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row][enemy.col - 1].setBackground(enemy_back);
                gameBoard[enemy.row][enemy.col - 1].setText("E");
                enemy.col--;
                enemy.id = gameBoard[enemy.row][enemy.col].getId();
            }
            else if(enemy.row < player.row && enemy.col == player.col && gameBoard[enemy.row + 1][enemy.col].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row + 1][enemy.col].setBackground(enemy_back);
                gameBoard[enemy.row + 1][enemy.col].setText("E");
                enemy.row++;
                enemy.id = gameBoard[enemy.row][enemy.col].getId();
            }
            else if(enemy.row > player.row && enemy.col == player.col && gameBoard[enemy.row - 1][enemy.col].getText() == "0") {
                gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                gameBoard[enemy.row][enemy.col].setText("0");
                gameBoard[enemy.row - 1][enemy.col].setBackground(enemy_back);
                gameBoard[enemy.row - 1][enemy.col].setText("E");
                enemy.row--;
                enemy.id = gameBoard[enemy.row][enemy.col].getId();
            }
            else {
                Random random = new Random();
                int randomRow = random.nextInt(3) - 1; // Генерирует случайное число от -1 до 1
                int randomCol = random.nextInt(3) - 1;
                if (!(enemy.row + randomRow >= 0 && enemy.row + randomRow < 8)) {
                    randomRow = 0;
                }
                if (!(enemy.col + randomCol >= 0 && enemy.col + randomCol < 8)) {
                    randomCol = 0;
                }
                if(gameBoard[enemy.row + randomRow][enemy.col + randomCol].getText() == "0") {
                    gameBoard[enemy.row][enemy.col].setBackground(empty_back);
                    gameBoard[enemy.row][enemy.col].setText("0");
                    gameBoard[enemy.row + randomRow][enemy.col + randomCol].setBackground(enemy_back);
                    gameBoard[enemy.row + randomRow][enemy.col + randomCol].setText("E");
                    enemy.row = enemy.row + randomRow;
                    enemy.col = enemy.col + randomCol;
                    enemy.id = gameBoard[enemy.row][enemy.col].getId();
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
