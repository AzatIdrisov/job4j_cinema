package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

public interface UserStore {
     void add(Account account, Ticket ticket);
     int findUserIdByEmail(Account account);
}
