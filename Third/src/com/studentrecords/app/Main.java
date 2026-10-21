package com.studentrecords.app;

import com.studentrecords.model.Student;
import com.studentrecords.service.StudentRecordService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentRecordService service =
        new StudentRecordService();

    public static void main(String[] args) {
        try {
            service.loadFromFile();
            System.out.println(
                "Loaded " + service.getStudentCount()
                + " student record(s)."
            );
        } catch (IOException e) {
            System.out.println(
                "Could not load records: " + e.getMessage()
            );
        }

        boolean running = true;

        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addStudent();
                        break;
                    case "2":
                        displayStudents(service.getAllStudents());
                        break;
                    case "3":
                        searchStudent();
                        break;
                    case "4":
                        displayStudents(service.sortByName());
                        break;
                    case "5":
                        displayStudents(service.sortByMarks());
                        break;
                    case "6":
                        searchByName();
                        break;
                    case "7":
                        removeStudent();
                        break;
                    case "8":
                        showStudentIds();
                        break;
                    case "9":
                        service.saveToFile();
                        System.out.println("Records saved successfully.");
                        break;
                    case "0":
                        service.saveToFile();
                        System.out.println("Records saved. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n=== Student Record Management ===");
        System.out.println("1. Add student");
        System.out.println("2. Display all students");
        System.out.println("3. Search by student ID");
        System.out.println("4. Sort by name");
        System.out.println("5. Sort by marks (highest first)");
        System.out.println("6. Search by name");
        System.out.println("7. Remove student");
        System.out.println("8. Display unique student IDs");
        System.out.println("9. Save records");
        System.out.println("0. Save and exit");
        System.out.print("Choose an option: ");
    }

    private static void addStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter marks (0-100): ");
        String marksInput = scanner.nextLine().trim();

        double marks;

        try {
            marks = Double.parseDouble(marksInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Marks must be a valid number."
            );
        }

        Student student = new Student(id, name, marks);
        service.addStudent(student);

        System.out.println("Student added successfully.");
    }

    private static void searchStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();

        Optional<Student> result = service.findStudent(id);

        result.ifPresentOrElse(
            System.out::println,
            () -> System.out.println("Student not found.")
        );
    }

    private static void searchByName() {
        System.out.print("Enter name or part of a name: ");
        String keyword = scanner.nextLine().trim();

        displayStudents(service.findByName(keyword));
    }

    private static void removeStudent() {
        System.out.print("Enter student ID to remove: ");
        String id = scanner.nextLine().trim();

        if (service.removeStudent(id)) {
            System.out.println("Student removed.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void showStudentIds() {
        Set<String> ids = service.getStudentIds();
        System.out.println("Unique student IDs: " + ids);
    }

    private static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        students.forEach(System.out::println);
        System.out.println("Records displayed: " + students.size());
    }
}