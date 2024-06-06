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


public class GameActivity extends AppCompatActivity {

    private final Entity player = new Entity(0,0,0);
    private static final List<Entity> enemies = new ArrayList<>();
    private static final List<Integer> ids = new ArrayList<>();

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
        Button[][] gameBoard = Mechanics.generateGameBoard();
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        initializeBoard(gridLayout, gameBoard);
    }



    private void initializeBoard(GridLayout gridLayout, char[][] gameBoard) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                Button boardCell = getBoardCell(row, col, gameBoard);
                ids.add(boardCell.getId());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0);

                gridLayout.addView(boardCell, params);
            }
        }

        int i = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                Button boardCell = findViewById(ids.get(i));
                int finalRow = row;
                int finalCol = col;
                boardCell.setOnClickListener(v -> boardCellClick(boardCell, gameBoard, finalRow, finalCol));
                ++i;
            }
        }

    }

    private void boardCellClick(Button boardCell, char[][] gameBoard, int row, int col) {
        if(boardCell.getText() == "0") {
            if (player.actions > 0 && Math.abs(player.row - row) <= 1 && Math.abs(player.col - col) <= 1) {
                Button prevCell = findViewById(player.id);
                prevCell.setBackground(empty_back);
                boardCell.setBackground(player_back);
                gameBoard[player.row][player.col] = '0';
                player.row = row;
                player.col = col;
                gameBoard[player.row][player.col] = 'P';
                player.id = boardCell.getId();
                --player.actions;
            }
        }
        if(boardCell.getText() == "E") {
            for(int i = 0; i < enemies.size(); ++i) {
                if(boardCell.getId() == enemies.get(i).id) {
                    Mechanics.playerAttack(player, enemies.get(i));
                    if(enemies.get(i).health <= 0) {
                        Button enemy = findViewById(enemies.get(i).id);
                        enemy.setBackground(dead_back);
                        gameBoard[row][col] = 'D';
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

    private Button getBoardCell(int row, int col, char[][] gameBoard) {
        Button boardCell;
        if(gameBoard[row][col] == '1') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.wallCell), null, 0);
            boardCell.setId(View.generateViewId());
            boardCell.setText("1");
            return boardCell;
        }
        if(gameBoard[row][col] == 'E') {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.enemyCell), null, 0);
            boardCell.setText("E");
            boardCell.setId(View.generateViewId());
            Entity enemy = new Entity(row, col, boardCell.getId());
            enemies.add(enemy);
            return boardCell;
        }
        if(gameBoard[row][col] == 'P') {
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
