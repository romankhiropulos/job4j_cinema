package ru.job4j.cinema.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.PsqlStorage;
import ru.job4j.cinema.persistence.Storage;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.List;

public class Cinema {

    private final Storage storage = PsqlStorage.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(Cinema.class.getName());

    private Cinema() {
    }

    private static final class Lazy {
        private static final Cinema INST = new Cinema();
    }

    public static Cinema getInstance() {
        return Cinema.Lazy.INST;
    }

    public Exception saveAccount(final Account account) {
        Exception answer = null;
        try {
//            account.setId((int) System.currentTimeMillis());
//            account.getTickets().forEach(t -> t.setAccountId((int) account.getId()));
            Account retAccount = storage.getAccount(account.getUsername(), account.getEmail(), account.getPhone());
            retAccount = (retAccount == null) ? storage.saveAccount(account) : storage.updateAccount(account);
        } catch (SQLIntegrityConstraintViolationException exception) {
            answer = exception;
            LOG.error("Constraint violation exception: " + exception.getMessage(), exception);
        } catch (SQLException exception) {
            answer = exception;
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
        }

        return answer;
    }

    public Collection<Ticket> getTickets() {
        Collection<Ticket> tickets = null;
        try {
            tickets = storage.getAllTickets();
        } catch (SQLException exception) {
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
        }

        return tickets;
    }

}
