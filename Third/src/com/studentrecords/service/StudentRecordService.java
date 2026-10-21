
package com.studentrecords.service;

import com.studentrecords.model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentRecordService {

    private final Map<String, Student> students = new HashMap<>();
    private final Set<String> studentIds = new HashSet<>();
    private final Path filePath = Paths.get("students.txt");

    public void addStudent(Student student) {
        if (studentIds.contains(student.getId())) {
            throw new IllegalArgumentException(
                "Student ID already exists."
            );
        }

        students.put(student.getId(), student);
        studentIds.add(student.getId());
    }

    public Optional<Student> findStudent(String id) {
        return Optional.ofNullable(students.get(id));
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Student> findByName(String keyword) {
        return students.values()
            .stream()
            .filter(s -> s.getName().toLowerCase()
                .contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Student> sortByName() {
        return students.values()
            .stream()
            .sorted(Comparator.comparing(
                Student::getName,
                String.CASE_INSENSITIVE_ORDER
            ))
            .collect(Collectors.toList());
    }

    public List<Student> sortByMarks() {
        return students.values()
            .stream()
            .sorted(
                Comparator.comparingDouble(Student::getMarks)
                    .reversed()
            )
            .collect(Collectors.toList());
    }

    public boolean removeStudent(String id) {
        Student removed = students.remove(id);

        if (removed != null) {
            studentIds.remove(id);
            return true;
        }

        return false;
    }

    public void saveToFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Student student : students.values()) {
                writer.write(
                    student.getId() + "|"
                    + student.getName() + "|"
                    + student.getMarks()
                );
                writer.newLine();
            }
        }
    }

    public void loadFromFile() throws IOException {
        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length != 3) {
                        throw new IllegalArgumentException(
                            "Expected ID, name and marks."
                        );
                    }

                    Student student = new Student(
                        parts[0].trim(),
                        parts[1].trim(),
                        Double.parseDouble(parts[2].trim())
                    );

                    if (!studentIds.contains(student.getId())) {
                        addStudent(student);
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "Skipping invalid line " + lineNumber
                        + ": " + e.getMessage()
                    );
                }
            }
        }
    }

    public Set<String> getStudentIds() {
        return new HashSet<>(studentIds);
    }

    public int getStudentCount() {
        return students.size();
    }
}
