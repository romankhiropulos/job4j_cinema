package ru.job4j.cinema.controller;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.Cinema;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class PaymentServlet extends HttpServlet {

    private final Cinema cinema = Cinema.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String strTickets = req.getParameter("tickets");
        List<Ticket> tickets = new ArrayList<>();
        String[] arr = strTickets.split(",");
        for (String str : arr) {
            String[] pair = str.split("_");
            tickets.add(new Ticket(1, Integer.parseInt(pair[0]), Integer.parseInt(pair[1])));
        }
        Account newAccount = new Account(
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("phone"),
                tickets
        );

        try {
            cinema.saveAccount(newAccount);
        } catch(SQLIntegrityConstraintViolationException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("Sorry, the selected tickets have already been purchased!");
            writer.flush();
        } catch(SQLException exception) {
            PrintWriter writer = resp.getWriter();
            writer.println("A user is already registered to this number / email!");
            writer.flush();
        }
    }
}
