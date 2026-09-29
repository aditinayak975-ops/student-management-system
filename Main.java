import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point of the Student Management System.
 * Presents a console menu for all CRUD, attendance and marks operations.
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static StudentDAO studentDAO = new StudentDAO();
    static AttendanceDAO attendanceDAO = new AttendanceDAO();
    static MarksDAO marksDAO = new MarksDAO();

    public static void main(String[] args) {
        int choice;
        do {
            printMainMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> registerStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> attendanceMenu();
                case 7 -> marksMenu();
                case 0 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice, try again.");
            }
        } while (choice != 0);

        DBConnection.closeConnection();
        sc.close();
    }

    // ---------------------------------------------------------------
    // MENUS
    // ---------------------------------------------------------------
    private static void printMainMenu() {
        System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Register New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Attendance Management");
        System.out.println("7. Marks Management");
        System.out.println("0. Exit");
    }

    private static void attendanceMenu() {
        int choice;
        do {
            System.out.println("\n--- Attendance Management ---");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Attendance by Student");
            System.out.println("3. View Attendance by Date");
            System.out.println("4. View Attendance Percentage");
            System.out.println("0. Back to Main Menu");
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> markAttendance();
                case 2 -> viewAttendanceByStudent();
                case 3 -> viewAttendanceByDate();
                case 4 -> viewAttendancePercentage();
                case 0 -> {}
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private static void marksMenu() {
        int choice;
        do {
            System.out.println("\n--- Marks Management ---");
            System.out.println("1. Add Marks");
            System.out.println("2. Update Marks");
            System.out.println("3. View Marks by Student");
            System.out.println("4. View Overall Percentage & Grade");
            System.out.println("0. Back to Main Menu");
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addMarks();
                case 2 -> updateMarks();
                case 3 -> viewMarksByStudent();
                case 4 -> viewOverallResult();
                case 0 -> {}
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    // ---------------------------------------------------------------
    // STUDENT OPERATIONS
    // ---------------------------------------------------------------
    private static void registerStudent() {
        System.out.println("\n--- Register New Student ---");
        String rollNo = readString("Roll No: ");
        String name = readString("Name: ");
        String course = readString("Course: ");
        int year = readInt("Year (1-4): ");
        String phone = readString("Phone: ");
        String email = readString("Email: ");
        String address = readString("Address: ");

        Student s = new Student(rollNo, name, course, year, phone, email, address);
        boolean success = studentDAO.addStudent(s);
        System.out.println(success ? "Student registered successfully!" : "Registration failed.");
    }

    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    private static void searchStudent() {
        String keyword = readString("\nEnter roll no or name to search: ");
        List<Student> results = studentDAO.searchStudents(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching students found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void updateStudent() {
        int id = readInt("\nEnter Student ID to update: ");
        Student existing = studentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Leave blank to keep the current value.");

        String name = readStringOrDefault("Name [" + existing.getName() + "]: ", existing.getName());
        String course = readStringOrDefault("Course [" + existing.getCourse() + "]: ", existing.getCourse());
        String phone = readStringOrDefault("Phone [" + existing.getPhone() + "]: ", existing.getPhone());
        String email = readStringOrDefault("Email [" + existing.getEmail() + "]: ", existing.getEmail());
        String address = readStringOrDefault("Address [" + existing.getAddress() + "]: ", existing.getAddress());

        existing.setName(name);
        existing.setCourse(course);
        existing.setPhone(phone);
        existing.setEmail(email);
        existing.setAddress(address);

        boolean success = studentDAO.updateStudent(existing);
        System.out.println(success ? "Student updated successfully!" : "Update failed.");
    }

    private static void deleteStudent() {
        int id = readInt("\nEnter Student ID to delete: ");
        boolean success = studentDAO.deleteStudent(id);
        System.out.println(success ? "Student deleted successfully!" : "Delete failed / student not found.");
    }

    // ---------------------------------------------------------------
    // ATTENDANCE OPERATIONS
    // ---------------------------------------------------------------
    private static void markAttendance() {
        int studentId = readInt("\nEnter Student ID: ");
        String dateStr = readString("Enter date (YYYY-MM-DD): ");
        String status = readString("Status (Present/Absent): ");

        boolean success = attendanceDAO.markAttendance(studentId, Date.valueOf(dateStr), status);
        System.out.println(success ? "Attendance recorded!" : "Failed to record attendance.");
    }

    private static void viewAttendanceByStudent() {
        int studentId = readInt("\nEnter Student ID: ");
        List<String> records = attendanceDAO.getAttendanceByStudent(studentId);
        if (records.isEmpty()) {
            System.out.println("No attendance records found.");
        } else {
            records.forEach(System.out::println);
        }
    }

    private static void viewAttendanceByDate() {
        String dateStr = readString("\nEnter date (YYYY-MM-DD): ");
        List<String> records = attendanceDAO.getAttendanceByDate(Date.valueOf(dateStr));
        if (records.isEmpty()) {
            System.out.println("No records found for this date.");
        } else {
            records.forEach(System.out::println);
        }
    }

    private static void viewAttendancePercentage() {
        int studentId = readInt("\nEnter Student ID: ");
        double pct = attendanceDAO.getAttendancePercentage(studentId);
        System.out.printf("Attendance Percentage: %.2f%%%n", pct);
    }

    // ---------------------------------------------------------------
    // MARKS OPERATIONS
    // ---------------------------------------------------------------
    private static void addMarks() {
        int studentId = readInt("\nEnter Student ID: ");
        String subject = readString("Subject: ");
        String examType = readString("Exam Type (Midterm/Final/Quiz): ");
        int semester = readInt("Semester: ");
        double marksObtained = readDouble("Marks Obtained: ");
        double maxMarks = readDouble("Max Marks: ");

        boolean success = marksDAO.addMarks(studentId, subject, examType, semester, marksObtained, maxMarks);
        System.out.println(success ? "Marks added successfully!" : "Failed to add marks.");
    }

    private static void updateMarks() {
        int markId = readInt("\nEnter Mark ID to update: ");
        double newMarks = readDouble("New Marks Obtained: ");
        boolean success = marksDAO.updateMarks(markId, newMarks);
        System.out.println(success ? "Marks updated successfully!" : "Update failed.");
    }

    private static void viewMarksByStudent() {
        int studentId = readInt("\nEnter Student ID: ");
        List<String> records = marksDAO.getMarksByStudent(studentId);
        if (records.isEmpty()) {
            System.out.println("No marks found for this student.");
        } else {
            records.forEach(System.out::println);
        }
    }

    private static void viewOverallResult() {
        int studentId = readInt("\nEnter Student ID: ");
        double pct = marksDAO.getOverallPercentage(studentId);
        String grade = marksDAO.calculateGrade(pct);
        System.out.printf("Overall Percentage: %.2f%%  |  Grade: %s%n", pct, grade);
    }

    // ---------------------------------------------------------------
    // INPUT HELPERS
    // ---------------------------------------------------------------
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine(); // consume newline
        return value;
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static String readStringOrDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = sc.nextLine();
        return input.isBlank() ? defaultValue : input;
    }
}
