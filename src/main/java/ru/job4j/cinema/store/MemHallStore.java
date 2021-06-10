package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Seat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MemHallStore implements HallStore {
    private static final MemHallStore INST = new MemHallStore();
    private final int rows = 3;
    private final int cols = 3;
    private final List<Seat> seats = new ArrayList<>(rows * cols);

    private MemHallStore() {
        int id = 0;
        for (int col = 1; col < cols; col++) {
            for (int row = 1; row < rows; row++) {
                seats.set(id, new Seat(id, true, row, col));
                id++;
            }
        }
    }

    public static MemHallStore instOf() {
        return INST;
    }

    @Override
    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public Seat getSeatById(int id) {
        return seats.get(id);
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
       for(int id: seatsId){
           seats.get(id).setAvailable(false);
       }
    }

    @Override
    public Set<Integer> getFilledIds() {
        Set<Integer> result = new HashSet<>();
        for(Seat seat: seats){
            if(!seat.isAvailable()){
                result.add(seat.getId());
            }
        }
        return result;
    }

}
