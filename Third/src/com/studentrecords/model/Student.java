
package com.studentrecords.model;

public class Student {
    private final String id;
    private final String name;
    private final double marks;

    public Student(String id, String name, double marks) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Student ID cannot be empty."
            );
        }

        if (name == null || name.trim().isEmpty()
                || name.contains("|")) {
            throw new IllegalArgumentException(
                "Enter a valid student name."
            );
        }

        if (!Double.isFinite(marks) || marks < 0 || marks > 100) {
            throw new IllegalArgumentException(
                "Marks must be between 0 and 100."
            );
        }

        this.id = id.trim();
        this.name = name.trim();
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %s | Name: %s | Marks: %.2f",
            id, name, marks
        );
    }
}
