package ru.job4j.cinema.model;

import java.util.List;
import java.util.Objects;

public class Account {

    private long id;
    private String username;
    private String email;
    private String phone;
    private List<Ticket> tickets;

    public Account(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public Account(long id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public Account(String username, String email, String phone, List<Ticket> tickets) {
        this(username, email, phone);
        this.tickets = tickets;
    }

    public Account(long id, String username, String email, String phone, List<Ticket> tickets) {
        this(id, username, email, phone);
        this.tickets = tickets;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) {
            return false;
        }
        if (!username.equals(account.username)) {
            return false;
        }
        if (!email.equals(account.email)) {
            return false;
        }
        return Objects.equals(phone, account.phone);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + username.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }
}
