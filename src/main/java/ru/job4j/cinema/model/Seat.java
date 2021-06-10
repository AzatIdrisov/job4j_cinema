package ru.job4j.cinema.model;

import java.util.Objects;

public class Seat {

    private final int id;
    private boolean isAvailable;
    private final int row;
    private final int col;

    public Seat(int id, boolean isAvailable, int row, int col) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.row = row;
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return id == seat.id && isAvailable == seat.isAvailable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isAvailable);
    }
}
