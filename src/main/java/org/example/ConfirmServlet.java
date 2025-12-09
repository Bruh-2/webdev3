package org.example;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;

public class ConfirmServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        JSONObject json = new JSONObject();

        if (token == null) {
            json.put("error", "token missing");
            resp.getWriter().print(json.toJSONString());
            return;
        }

        try (Connection c = DbHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE users SET confirmed=1 WHERE token=?"
            );
            ps.setString(1, token);
            int updated = ps.executeUpdate();

            if (updated > 0) json.put("message", "confirmed");
            else json.put("error", "wrong token");

        } catch (SQLException e) {
            json.put("error", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().print(json.toJSONString());
    }
}
