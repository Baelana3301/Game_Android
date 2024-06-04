package com.example.dawnofdesolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mechanics {
    private static final int ROWS = 10;
    private static final int COLS = 8;
    private static final char EMPTY_CELL = '0';
    private static final char PLAYER = 'P';
    private static final char ENEMY = 'E';
    private static final char WALL = '1';

    public static char[][] generateGameBoard() {
        char[][] board = new char[ROWS][COLS];

        // Список всех возможных ячеек
        List<Character> cells = new ArrayList<>();

        // Добавляем "1" (стены), заполняем 30% клеток стены
        int totalWalls = (int) (ROWS * COLS * 0.2);
        for (int i = 0; i < totalWalls; i++) {
            cells.add(WALL);
        }

        // Добавляем "0" (пустые клетки) на оставшееся количество клеток, оставив место для героя и врагов
        int remainingEmptyCells = (ROWS * COLS) - totalWalls - 1 - 4;
        for (int i = 0; i < remainingEmptyCells; i++) {
            cells.add(EMPTY_CELL);
        }

        // Добавляем "p" (главный персонаж)
        cells.add(PLAYER);

        // Добавляем "E" (враги, четыре экземпляра)
        for (int i = 0; i < 4; i++) {
            cells.add(ENEMY);
        }

        // Перемешиваем все ячейки случайным образом
        Collections.shuffle(cells);

        // Заполняем двумерный массив игрового поля
        int index = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = cells.get(index++);
            }
        }

        return board;
    }
}
