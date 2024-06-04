package com.example.dawnofdesolation;

public class Entity {
    int health = 10;
    int row, col;
    int actions = 2;
    int id;

    // Конструктор класса Entity
    public Entity(int row, int col, int id) {
        this.row = row;
        this.col = col;
        this.id = id;
    }
}
