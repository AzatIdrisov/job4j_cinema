package ru.job4j.cinema.servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.job4j.cinema.model.Seat;
import ru.job4j.cinema.store.PsqlHallStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Seat> seats = PsqlHallStore.instOf().getSeats();
        JSONObject obj = new JSONObject();
        obj.put("rows", PsqlHallStore.instOf().getNumOfRows());
        obj.put("cols", PsqlHallStore.instOf().getNumOfCols());
        JSONArray arr = new JSONArray();
        for (Seat seat : seats) {
            JSONObject seatObj = new JSONObject();
            seatObj.put("id", "" + seat.getId());
            seatObj.put("available", "" + seat.isAvailable());
            arr.add(seatObj);
        }
        obj.put("seats", arr);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.print(obj.toJSONString());
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<Seat> seats = PsqlHallStore.instOf().getSeats();
        List<Integer> reservedSeats = new ArrayList<>();
        Set<Integer> unavailableSeats = PsqlHallStore.instOf().getFilledIds();
        for (Seat seat : seats) {
            if (req.getParameter("seat" + seat.getId()) != null && !unavailableSeats.contains(seat.getId())) {
                reservedSeats.add(seat.getId());
            }
        }
        req.setAttribute("reserved", reservedSeats);
        req.getRequestDispatcher("/payment.jsp").forward(req, resp);
    }
}
