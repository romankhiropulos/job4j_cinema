package ru.job4j.cinema.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.Cinema;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 1. Данные на index.html должны загружать через Ajax.
//
// 2. Для этого создайте сервлет HallServlet.
//
// 3. Если место занято, то нужно это отображать в таблице.
//
// 4. Страница должно обновлять периодически через timout.
//
public class HallServlet extends HttpServlet {

    private final Cinema cinema = Cinema.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setHeader("cache-control", "no-cache");

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        List<Ticket> tickets = (List<Ticket>) cinema.getTickets();
//        tickets.sort(Ticket::compareTo);
//        Collections.sort(tickets);
        tickets.sort(Comparator.comparingInt(Ticket::getRow));
        tickets.sort(Comparator.comparingInt(Ticket::getCell));
//        Comparator<Ticket> RESUME_COMPARATOR = (o1, o2) -> o1.getRow() < o2.getCell();
        String jsonResponse = gson.toJson(tickets);
        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}
