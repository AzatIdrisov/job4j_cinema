package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Seat;
import ru.job4j.cinema.model.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class PsqlUserStore implements UserStore{
    private static final Logger LOG = LoggerFactory.getLogger(PsqlHallStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlUserStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error("Exception when loading db properties", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error("Exception when registering JDBC drive", e);
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
        private static final UserStore INST = new PsqlUserStore();
    }

    public static UserStore instOf() {
        return Lazy.INST;
    }

    @Override
    public void add(Account account, Ticket ticket) {
        saveAccount(account);
        saveTicket(account, ticket);
    }

    @Override
    public int findUserIdByEmail(Account account) {
        int id = -1;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from account where email = ?")
        ) {
            ps.setString(1, account.getEmail());
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    id = it.getInt("id");
                }
            }
        } catch (Exception e) {
            LOG.error("Exception when finding account", e);
        }
        return id;
    }

    private void saveAccount (Account account){
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO account(username, email, phone) VALUES (?, ?, ?)")
        ) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception when adding account", e);
        }
    }

    private void saveTicket (Account account, Ticket ticket){
        int accountId = findUserIdByEmail(account);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO ticket(session_id, row, col, account_id) VALUES (?, ?, ?, ?)")
        ) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCol());
            ps.setInt(4, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception when adding ticket", e);
        }
    }


}
