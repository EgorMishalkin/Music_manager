package laba2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Подключение к базе
        String url = "jdbc:mysql://localhost:3306/music_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "443081443081";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            String sql = "SELECT id, name, founded_year FROM music_groups";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("Группы в базе данных:");

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int foundedYear = rs.getInt("founded_year");
                System.out.println(id + ". " + name + " (основана в " + foundedYear + ")");
            }

            if (!hasRows) {
                System.out.println("Таблица music_groups пока пустая!");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Ошибка при работе с базой данных!");
            e.printStackTrace();
        }
    }
}
