package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Seat;

import java.util.List;
import java.util.Set;

public interface HallStore {
    List<Seat> getSeats();
    Seat getSeatById(int id);
    int getNumOfRows();
    int getNumOfCols();
    void fillSeats(List<Integer> seatsId);
    Set<Integer> getFilledIds();
}
