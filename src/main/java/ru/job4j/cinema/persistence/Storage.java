package ru.job4j.cinema.persistence;

import ru.job4j.cinema.model.Account;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public interface Storage {

    Account saveAccount(Account account) throws SQLIntegrityConstraintViolationException, SQLException;

    Account updateAccount(Account account) throws SQLIntegrityConstraintViolationException, SQLException;

    Account getAccount(String username, String email, String phone) throws SQLException;
}
