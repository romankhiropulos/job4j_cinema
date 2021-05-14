package ru.job4j.cinema.controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.Cinema;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 5. После того как пользователь выбрал место, нужно перейти на страницу payment.html.
//    Сделать это можно через JS - window.local.href.
//
// На странице нужно указать место и сумму денег.
//
public class PaymentServlet extends HttpServlet {

    private final Cinema cinema = Cinema.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

//        https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson
        String strTickets = req.getParameter("tickets");
        JsonParser jsonParser = new JsonParser();
        JsonObject data = (JsonObject) jsonParser.parse(strTickets);
        JsonArray accounts = data.getAsJsonArray("data");
        List<Ticket> tickets = new Gson().fromJson(accounts.toString(), new TypeToken<ArrayList<Ticket>>() {
        }.getType());

        Account newAccount = new Account(
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("phone"),
                tickets
        );

        if (cinema.saveAccount(newAccount)) {
//            sc.setAttribute("user", newUser);
//            resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
//            req.setAttribute("error", "Билет уже куплен!");
//            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }



//        if (PsqlStore.instOf().findUserByEmail(newUser.getEmail()) != null) {
//            req.setAttribute("error", "К этому email'у уже привязан пользователь");
//            req.getRequestDispatcher("reg.jsp").forward(req, resp);
//        } else {
//            PsqlStore.instOf().save(newUser);
//            HttpSession sc = req.getSession();
//            sc.setAttribute("user", newUser);
//            resp.sendRedirect(req.getContextPath() + "/index.jsp");
//        }
    }
}
