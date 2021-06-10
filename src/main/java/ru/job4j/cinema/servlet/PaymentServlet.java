package ru.job4j.cinema.servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Seat;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.PsqlHallStore;
import ru.job4j.cinema.store.PsqlUserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reservedString = req.getParameter("reserved");
        List<Integer> reservedIds = parseReservedIdFromStr(reservedString);
        String name = req.getParameter("username");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        Account account = new Account(0,name, email, phone);
        List<Seat> seats = PsqlHallStore.instOf().getSeats();
        for (Seat seat : seats) {
            if (reservedIds.contains(seat.getId())) {
                PsqlUserStore.instOf().add(account, new Ticket(seat.getId(),1, seat.getRow(),seat.getCol()));
            }
        }
        PsqlHallStore.instOf().fillSeats(reservedIds);
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    private List<Integer> parseReservedIdFromStr(String resStr) {
        String[] parts = resStr.split("x");
        List<Integer> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Integer.parseInt(part.trim()));
        }
        return result;
    }
}
