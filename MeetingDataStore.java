import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeetingDataStore {

    // Database Configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/meeting_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "YOUR_MYSQL_PASSWORD";

    // Method to obtain Database Connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // CREATE: Save meeting to MySQL database
    public static boolean addMeeting(Meeting meeting) {
        String sql = "INSERT INTO meetings (meeting_id, title, department, meeting_date, meeting_time, venue) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meeting.getId());
            stmt.setString(2, meeting.getTitle());
            stmt.setString(3, meeting.getDepartment());
            stmt.setString(4, meeting.getDate());
            stmt.setString(5, meeting.getTime());
            stmt.setString(6, meeting.getRoom());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Retrieve all meetings from MySQL database
    public static List<Meeting> getAllMeetings() {
        List<Meeting> list = new ArrayList<>();
        String sql = "SELECT * FROM meetings";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Meeting m = new Meeting(
                        rs.getString("meeting_id"),
                        rs.getString("title"),
                        rs.getString("department"),
                        rs.getString("meeting_date"),
                        rs.getString("meeting_time"),
                        rs.getString("venue")
                );
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // DELETE: Remove meeting from MySQL database by ID
    public static boolean deleteMeeting(String meetingId) {
        String sql = "DELETE FROM meetings WHERE meeting_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meetingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}