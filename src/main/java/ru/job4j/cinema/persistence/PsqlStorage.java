package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
                    account.getTickets().forEach(t -> t.setAccountId(account.getId()));
                }
            }
            updateTicketsHolder(cn, account);
        }
        return account;
    }

    public Account updateAccount(Account account) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET username =  ? WHERE id = ?")
        ) {
            updateTicketsHolder(cn, account);
        }

        return account;
    }

    private void updateTicketsHolder(Connection connection, Account account) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE ticket SET account_id = ? WHERE session_id = 1"
                        + " AND row = ?"
                        + " AND cell = ?"
        )) {
            for (Ticket ticket : account.getTickets()) {
                ps.setInt(1, ticket.getAccountId());
                ps.setInt(2, ticket.getRow());
                ps.setInt(3, ticket.getCell());
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
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        }

        return account;
    }

    @Override
    public Collection<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(
                            new Ticket(it.getInt("id"),
                                    it.getInt("session_id"),
                                    it.getInt("row"),
                                    it.getInt("cell"),
                                    it.getInt("account_id")
                            )
                    );
                }
            }
        }

        return tickets;
    }

    @Override
    public Collection<Ticket> findTicketsByAccountId(int accountId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket WHERE account_id = ?")
        ) {
            ps.setInt(1, accountId);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(
                            new Ticket(it.getInt("id"),
                                    it.getInt("session_id"),
                                    it.getInt("row"),
                                    it.getInt("cell"),
                                    it.getInt("account_id")
                            )
                    );
                }
            }
        }

        return tickets;
    }
}
