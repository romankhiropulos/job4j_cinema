package ru.job4j.cinema.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.persistence.PsqlStorage;
import ru.job4j.cinema.persistence.Storage;

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

    public boolean saveAccount(Account account) {

        boolean result = true;
        try {
            storage.save(account);
        } catch (Exception exception) {
            result = false;
            LOG.error("Exception: " + exception.getMessage(), exception);
        }

        return result;
    }
}
