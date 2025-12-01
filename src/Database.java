import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Database {
    private static final Logger log = Logger.getLogger(Database.class);

    private static final String URL = "jdbc:mysql://localhost:3306/music_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "443081443081";

    public static List<String> getGroups() {
        List<String> groups = new ArrayList<>();
        log.info("Начата загрузка списка групп из базы данных");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, founded_year FROM music_groups");

            while (rs.next()) {
                String name = rs.getString("name");
                int year = rs.getInt("founded_year");
                groups.add(name + " (" + year + ")");
            }

            rs.close();
            stmt.close();
            conn.close();
            log.info("Список групп успешно загружен из базы данных");
        } catch (Exception e) {
            log.error("Ошибка при загрузке групп из базы данных", e);
            groups.add("⚠ Ошибка загрузки групп!");
        }

        return groups;
    }
}
