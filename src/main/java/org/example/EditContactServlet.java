package org.example;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;

public class EditContactServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");

        JSONObject json = new JSONObject();

        if (id == null || name == null || phone == null) {
            json.put("error", "missing fields");
            resp.getWriter().print(json.toJSONString());
            return;
        }

        try (Connection c = DbHelper.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE contacts SET name=?, phone=? WHERE id=?"
            );
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setInt(3, Integer.parseInt(id));

            int ok = ps.executeUpdate();

            if (ok > 0) json.put("message", "updated");
            else json.put("error", "no such id");

        } catch (Exception e) {
            json.put("error", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().print(json.toJSONString());
    }
}
