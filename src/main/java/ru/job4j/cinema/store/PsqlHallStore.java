package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Seat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PsqlHallStore implements HallStore {
    private final int rows = 3;
    private final int cols = 3;
    private static final Logger LOG = LoggerFactory.getLogger(PsqlHallStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlHallStore() {
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
        private static final HallStore INST = new PsqlHallStore();
    }

    public static HallStore instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall order by id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    seats.add(new Seat(it.getInt("id"),
                            it.getBoolean("isAvailable"),
                            it.getInt("row"),
                            it.getInt("col")));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            LOG.error("Exception when extracting all items from hall table", e);
        }
        return seats;
    }

    @Override
    public Seat getSeatById(int id) {
        Seat seat = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    seat = new Seat(it.getInt("id"),
                            it.getBoolean("isAvailable"),
                            it.getInt("row"),
                            it.getInt("col"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception when searching seat by id", e);
        }
        return seat;
    }

    @Override
    public int getNumOfRows() {
        return rows;
    }

    @Override
    public int getNumOfCols() {
        return cols;
    }

    @Override
    public void fillSeats(List<Integer> seatsId) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("update hall set isAvailable = false where id = ?")
        ) {
            for (int id : seatsId) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            LOG.error("Exception when filling seats in hall bd", e);
        }
    }

    @Override
    public Set<Integer> getFilledIds() {
        Set<Integer> result = new HashSet<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall where isAvailable = false")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(it.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception when extracting filled seats from hall table", e);
        }
        return result;
    }

}
