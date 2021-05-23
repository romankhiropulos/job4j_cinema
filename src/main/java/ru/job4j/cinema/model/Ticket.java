package ru.job4j.cinema.model;

public class Ticket {
    private int id;
    private int sessionId;
    private int row;
    private int cell;
    private int accountId;

    public Ticket(int sessionId, int row, int cell) {
        this.sessionId = sessionId;
        this.row = row;
        this.cell = cell;
    }

    public Ticket(int id, int sessionId, int row, int cell, int accountId) {
        this.id = id;
        this.sessionId = sessionId;
        this.row = row;
        this.cell = cell;
        this.accountId = accountId;
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

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

        if (id != ticket.id) {
            return false;
        }
        if (sessionId != ticket.sessionId) {
            return false;
        }
        if (row != ticket.row) {
            return false;
        }
        if (cell != ticket.cell) {
            return false;
        }
        return accountId == ticket.accountId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + sessionId;
        result = 31 * result + row;
        result = 31 * result + cell;
        result = 31 * result + accountId;
        return result;
    }
}
