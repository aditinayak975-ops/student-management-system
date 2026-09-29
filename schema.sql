-- ==========================================================
-- Student Management System - Database Schema
-- ==========================================================

CREATE DATABASE IF NOT EXISTS student_management_system;
USE student_management_system;

-- ----------------------------------------------------------
-- Table: students
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    student_id      INT AUTO_INCREMENT PRIMARY KEY,
    roll_no         VARCHAR(20)  NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    course          VARCHAR(100) NOT NULL,
    year            INT          NOT NULL,
    phone           VARCHAR(15),
    email           VARCHAR(100),
    address         VARCHAR(255),
    date_of_admission DATE DEFAULT (CURRENT_DATE)
);

-- ----------------------------------------------------------
-- Table: attendance
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id   INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    attendance_date DATE NOT NULL,
    status          ENUM('Present', 'Absent') NOT NULL,
    CONSTRAINT fk_attendance_student
        FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE,
    UNIQUE KEY uniq_student_date (student_id, attendance_date)
);

-- ----------------------------------------------------------
-- Table: marks
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS marks (
    mark_id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    subject         VARCHAR(100) NOT NULL,
    exam_type       VARCHAR(50)  NOT NULL,   -- e.g. Midterm, Final, Quiz
    semester        INT NOT NULL,
    marks_obtained  DECIMAL(5,2) NOT NULL,
    max_marks       DECIMAL(5,2) NOT NULL DEFAULT 100,
    CONSTRAINT fk_marks_student
        FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- ----------------------------------------------------------
-- Sample seed data (optional)
-- ----------------------------------------------------------
INSERT INTO students (roll_no, name, course, year, phone, email, address)
VALUES
('CS101', 'Aditi Sharma', 'B.Tech CSE', 2, '9876543210', 'aditi@example.com', 'Cuttack, Odisha'),
('CS102', 'Rohit Verma', 'B.Tech CSE', 2, '9876500000', 'rohit@example.com', 'Bhubaneswar, Odisha');

INSERT INTO attendance (student_id, attendance_date, status) VALUES
(1, '2026-07-01', 'Present'),
(1, '2026-07-02', 'Absent'),
(2, '2026-07-01', 'Present');

INSERT INTO marks (student_id, subject, exam_type, semester, marks_obtained, max_marks) VALUES
(1, 'Data Structures', 'Midterm', 3, 42, 50),
(1, 'DBMS', 'Midterm', 3, 45, 50),
(2, 'Data Structures', 'Midterm', 3, 38, 50);
