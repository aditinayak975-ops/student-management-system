import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object handling subject-wise marks for students.
 */
public class MarksDAO {

    // ---- Add a mark entry ----
    public boolean addMarks(int studentId, String subject, String examType,
                             int semester, double marksObtained, double maxMarks) {
        String sql = "INSERT INTO marks (student_id, subject, exam_type, semester, marks_obtained, max_marks) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, subject);
            ps.setString(3, examType);
            ps.setInt(4, semester);
            ps.setDouble(5, marksObtained);
            ps.setDouble(6, maxMarks);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding marks: " + e.getMessage());
            return false;
        }
    }

    // ---- Update an existing mark entry ----
    public boolean updateMarks(int markId, double marksObtained) {
        String sql = "UPDATE marks SET marks_obtained = ? WHERE mark_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, marksObtained);
            ps.setInt(2, markId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating marks: " + e.getMessage());
            return false;
        }
    }

    // ---- View all marks for one student ----
    public List<String> getMarksByStudent(int studentId) {
        List<String> records = new ArrayList<>();
        String sql = "SELECT mark_id, subject, exam_type, semester, marks_obtained, max_marks " +
                     "FROM marks WHERE student_id = ? ORDER BY semester, subject";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(String.format(
                        "[ID:%d] Sem %d | %-20s | %-10s | %.2f / %.2f",
                        rs.getInt("mark_id"), rs.getInt("semester"),
                        rs.getString("subject"), rs.getString("exam_type"),
                        rs.getDouble("marks_obtained"), rs.getDouble("max_marks")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching marks: " + e.getMessage());
        }
        return records;
    }

    // ---- Compute overall percentage for a student across all recorded marks ----
    public double getOverallPercentage(int studentId) {
        String sql = "SELECT SUM(marks_obtained) AS total_obtained, SUM(max_marks) AS total_max " +
                     "FROM marks WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double totalObtained = rs.getDouble("total_obtained");
                    double totalMax = rs.getDouble("total_max");
                    if (totalMax == 0) return 0.0;
                    return (totalObtained * 100.0) / totalMax;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error computing percentage: " + e.getMessage());
        }
        return 0.0;
    }

    // ---- Simple grade calculator based on percentage ----
    public String calculateGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }
}
