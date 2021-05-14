package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {
    private int id;
    private int session_id;
    private int row;
    private int cell;
    private Account account;

    public Ticket(int session_id, int row, int cell) {
        this.session_id = session_id;
        this.row = row;
        this.cell = cell;
    }

    public Ticket(int session_id, int row, int cell, Account account) {
        this(session_id, row, cell);
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        if (session_id != ticket.session_id) {
            return false;
        }
        if (row != ticket.row) {
            return false;
        }
        if (cell != ticket.cell) {
            return false;
        }
        return Objects.equals(account, ticket.account);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + session_id;
        result = 31 * result + row;
        result = 31 * result + cell;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
