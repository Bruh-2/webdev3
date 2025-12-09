package org.example;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;

public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        JSONObject json = new JSONObject();

        if (email == null || pass == null) {
            json.put("error", "missing fields");
            resp.getWriter().print(json.toJSONString());
            return;
        }

        String token = java.util.UUID.randomUUID().toString();

        try (Connection c = DbHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO users(email,password,token) VALUES(?,?,?)"
            );
            ps.setString(1, email);
            ps.setString(2, pass); // в учебных целях, не хэшируем
            ps.setString(3, token);
            ps.executeUpdate();

            json.put("message", "user created");
            json.put("confirm_link", "http://localhost:8080/WebDev3/confirm?token=" + token);

        } catch (SQLException e) {
            json.put("error", e.getMessage());
        }

        resp.getWriter().print(json.toJSONString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("SignupServlet is working!");
    }
}
