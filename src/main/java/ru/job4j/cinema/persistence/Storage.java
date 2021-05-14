package ru.job4j.cinema.persistence;

import ru.job4j.cinema.model.Account;

public interface Storage {

    boolean save(Account account);
}
