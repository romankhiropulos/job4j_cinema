package ru.job4j.cinema;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.PsqlStorage;
import ru.job4j.cinema.persistence.Storage;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Storage storage = PsqlStorage.getInstance();

        try {
            List<Ticket> tickets = (List<Ticket>) storage.getAllTickets();
            tickets.forEach(System.out::println);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
