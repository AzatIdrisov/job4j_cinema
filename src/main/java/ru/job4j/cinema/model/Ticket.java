package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {

   private int id;
   private int sessionId;
   private int row;
   private int col;

    public Ticket(int id, int sessionId, int row, int col) {
        this.id = id;
        this.sessionId = sessionId;
        this.row = row;
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id
                && sessionId == ticket.sessionId
                && row == ticket.row
                && col == ticket.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, row, col);
    }
}
