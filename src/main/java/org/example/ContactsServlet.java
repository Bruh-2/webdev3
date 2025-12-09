package org.example;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class ContactsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        JSONArray arr = new JSONArray();

        try (Connection c = DbHelper.getConnection()) {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM contacts");

            while (rs.next()) {
                JSONObject o = new JSONObject();
                o.put("id", rs.getInt("id"));
                o.put("name", rs.getString("name"));
                o.put("phone", rs.getString("phone"));
                arr.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.getWriter().print(arr.toJSONString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");

        JSONObject json = new JSONObject();

        if (name == null || phone == null) {
            json.put("error", "missing fields");
            resp.getWriter().print(json.toJSONString());
            return;
        }

        try (Connection c = DbHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO contacts(name,phone) VALUES(?,?)"
            );
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.executeUpdate();

        } catch (SQLException e) {
            json.put("error", e.getMessage());
            resp.getWriter().print(json.toJSONString());
            return;
        }

        // Возвращаем список всех контактов
        doGet(req, resp);
    }
}
