package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class PsqlStorage implements Storage {

    private final BasicDataSource pool = new BasicDataSource();

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStorage.class.getName());

    private PsqlStorage() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Storage INST = new PsqlStorage();
    }

    public static Storage getInstance() {
        return Lazy.INST;
    }

    @Override
    public Account saveAccount(Account account) throws SQLIntegrityConstraintViolationException, SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO account(username, email, phone) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    account.setId(id.getInt(1));
                }
            }
            insertTickets(cn, account);
        }
        return account;
    }

    public Account updateAccount(Account account) throws SQLIntegrityConstraintViolationException, SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET username =  ? WHERE id = ?")
        ) {
            insertTickets(cn, account);
        }

        return account;
    }

    private void insertTickets(Connection connection, Account account)
            throws SQLIntegrityConstraintViolationException, SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO ticket(session_id, row, cell, account_id) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            for (Ticket ticket : account.getTickets()) {
                ps.setInt(1, 1);
                ps.setInt(2, ticket.getRow());
                ps.setInt(3, ticket.getCell());
                ps.setLong(4, ticket.getAccount().getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public Account getAccount(String username, String email, String phone) throws SQLException {
        Account account = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM account WHERE username = ?"
                     + "AND email = ?"
                     + "AND phone = ?")
        ) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account = new Account(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        }

        return account;
    }

}
