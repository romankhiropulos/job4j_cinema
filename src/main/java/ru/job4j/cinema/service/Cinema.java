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

    public void saveAccount(final Account account) throws SQLException {
        SQLException resultException;
        try {
            if (!validateChosenTickets(account)) {
                resultException = new SQLIntegrityConstraintViolationException("Tickets are already sold out!");
                LOG.error("Constraint violation exception: " + resultException.getMessage(), resultException);
                throw resultException;
            }
            Account retAccount = storage.getAccount(account.getUsername(), account.getEmail(), account.getPhone());
            if (retAccount == null) {
                storage.saveAccount(account);
            } else {
                account.getTickets().forEach(t -> t.setAccountId(retAccount.getId()));
                storage.updateAccount(account);
            }
        } catch (SQLIntegrityConstraintViolationException exception) {
            resultException = exception;
            LOG.error("Constraint violation exception: " + exception.getMessage(), exception);
            throw resultException;
        } catch (SQLException exception) {
            resultException = exception;
            LOG.error("SQL Exception: " + exception.getMessage(), exception);
            throw resultException;
        }
    }

    private boolean validateChosenTickets(Account account) throws SQLException {
        boolean result = true;
        List<Ticket> heldTickets = (List<Ticket>) storage.getHeldTickets();
        for (Ticket accountTicket : account.getTickets()) {
            for (Ticket heldTicket : heldTickets) {
                if (accountTicket.getRow() == heldTicket.getRow()
                        && accountTicket.getCell() == heldTicket.getCell()) {
                    result = false;
                    break;
                }
            }
        }
        return result;
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
