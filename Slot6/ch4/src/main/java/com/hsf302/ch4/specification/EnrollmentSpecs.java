package com.hsf302.ch4.specification;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class EnrollmentSpecs {

    public static Specification<Student> enrolledIn(String courseCode) {
        return (root, query, cb) -> {
            if (courseCode == null || courseCode.isBlank()) return null;
            Join<Student, Course> courses = root.join("courses");
            return cb.equal(courses.get("code"), courseCode);
        };
    }

    public static Specification<Student> inSemester(String semester) {
        return (root, query, cb) -> {
            if (semester == null || semester.isBlank()) return null;
            query.distinct(true);
            Join<Student, Course> courses = root.join("courses");
            return cb.equal(courses.get("semester"), semester);
        };
    }

    public static Specification<Student> inDepartment(String deptCode) {
        return (root, query, cb) -> {
            if (deptCode == null || deptCode.isBlank()) return null;
            return cb.equal(root.get("department").get("code"), deptCode);
        };
    }

    public static Specification<Student> gpaAtLeast(Double minGpa) {
        return (root, query, cb) -> {
            if (minGpa == null) return null;
            return cb.ge(root.get("gpa"), minGpa);
        };
    }
}