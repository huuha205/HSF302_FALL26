package com.hsf302.ch4.runner;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.service.CourseService;
import com.hsf302.ch4.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class Exercise2Runner implements CommandLineRunner {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Override
    public void run(String... args) {
        todo6();
        todo7();
        todo8();
    }

    private void todo6() {
        title("TODO 6: count, findAll(Sort), findById");

        System.out.println("Total courses: " + courseService.count());

        printList(
                "All courses order by code",
                courseService.findAllOrderByCode()
        );

        for (long id : new long[]{2L, 99L}) {
            System.out.println(
                    "findById(" + id + "): "
                            + courseService.findById(id)
                            .map(Course::toString)
                            .orElse("Not found")
            );
        }
    }

    private void todo7() {
        title("TODO 7: navigate student.getCourses() / course.getStudents()");
        printList("(a) Courses of SE001", enrollmentService.getCoursesOfStudent("SE001"));
        printList("(b) Students of AIL303", enrollmentService.getStudentsOfCourse("AIL303"));
    }

    private void todo8() {
        title("TODO 8: findByCode, findBySemester, countBySemester");
        for (String code : List.of("HSF302", "XXX000")) {
            System.out.println("(a) " + code + ": "
                    + courseService.findByCode(code).map(Course::getName).orElse("Not found"));
        }
        printList("(b) Semester SU26", courseService.findBySemester("SU26"));
        System.out.println("(c) Courses in FA26: " + courseService.countBySemester("FA26"));
    }

    private void title(String text) {
        System.out.println();
        System.out.println("===== " + text + " =====");
    }

    private void printList(String label, java.util.Collection<?> list) {
        System.out.println("-- " + label + ":");

        list.forEach(o ->
                System.out.println("   " + o)
        );

        System.out.println("   -> " + list.size() + " record(s)");
    }
}