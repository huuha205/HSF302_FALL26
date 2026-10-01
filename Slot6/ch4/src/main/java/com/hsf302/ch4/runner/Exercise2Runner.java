package com.hsf302.ch4.runner;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.service.CourseService;
import com.hsf302.ch4.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.CommandLineRunner;

import java.util.Comparator;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class Exercise2Runner implements CommandLineRunner {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final com.hsf302.ch4.service.StudentService studentService;

    @Override
    public void run(String... args) {
        todo6();
        todo7();
        todo8();
        todo9();
        todo10();
        todo11();
        todo12();
        todo13();
        todo14();
        todo15();
        todo16();
        todo17();
        todo18();
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

    private void todo9() {
        title("TODO 9: derived query through collection courses");
        printList("(a) Students of PRJ301", enrollmentService.findStudentsInCourse("PRJ301"));
        System.out.println("(b) Students of HSF302: " + enrollmentService.countStudentsInCourse("HSF302"));
        printList("(c) Active students of PRJ301", enrollmentService.findActiveStudentsInCourse("PRJ301"));
    }

    private void todo10() {
        title("TODO 10: derived query from inverse side, Distinct");
        printList("(a) Courses of SE002", courseService.findCoursesOfStudent("SE002"));
        printList("(b1) Courses of AI students - no Distinct", courseService.findCoursesOfDepartment("AI", false));
        printList("(b2) Courses of AI students - Distinct", courseService.findCoursesOfDepartment("AI", true));
    }

    private void todo11() {
        title("TODO 11: IsEmpty, existsBy...And...");
        printList("(a) Students without courses", enrollmentService.findStudentsWithoutCourses());
        printList("(b) Courses without students", courseService.findCoursesWithoutStudents());
        System.out.println("(c) SE001 enrolled AIL303? " + enrollmentService.isEnrolled("SE001", "AIL303"));
        System.out.println("    SE002 enrolled AIL303? " + enrollmentService.isEnrolled("SE002", "AIL303"));
    }

    private void todo12() {
        title("TODO 12: JPQL JOIN s.courses");
        printList("HSF302 & GPA >= 3.5", enrollmentService.findGoodStudentsInCourse("HSF302", 3.5));
    }

    private void todo13() {
        title("TODO 13: course statistics (LEFT JOIN + GROUP BY + DTO)");
        printCourseStats();
    }

    private void printCourseStats() {
        courseService.getStatistics().forEach(d -> System.out.printf(
                "   %-6s | %-40s | %d/%d (free %d) | avg GPA %s%n",
                d.code(), d.name(), d.enrolled(), d.capacity(), d.remaining(),
                d.avgGpa() == null ? "null" : String.format("%.3f", d.avgGpa())));
    }

    private void todo14() {
        title("TODO 14: total credits per student (GROUP BY + HAVING)");
        enrollmentService.getCreditSummary(7).forEach(d -> System.out.printf(
                "   %s | %-15s | %d course(s) | %d credits%n",
                d.studentCode(), d.fullName(), d.courseCount(), d.totalCredits()));
    }

    private void todo15() {
        title("TODO 15: SIZE() on collections");
        printList("(a) Full courses", courseService.findFullCourses());
        printList("(b) Students with more than 2 courses", enrollmentService.findStudentsWithMoreThan(2));
    }

    private void todo16() {
        title("TODO 16: LazyInitializationException, JOIN FETCH, @EntityGraph");

        // (a) Student trả về từ Service (Exercise 1) → transaction đã đóng → courses chưa được nạp
        try {
            com.hsf302.ch4.pojo.Student s = studentService.findByStudentCode("SE001").orElseThrow();
            System.out.println("(a) courses = " + s.getCourses().size());
        } catch (LazyInitializationException e) {
            System.out.println("(a) Caught: " + e.getClass().getSimpleName());
            System.out.println("    " + e.getMessage());
        }

        // (b) JOIN FETCH: nạp student + courses trong 1 câu SQL
        com.hsf302.ch4.pojo.Student s = enrollmentService.getStudentWithCourses("SE001");
        System.out.println("(b) " + s.getStudentCode() + " - " + s.getFullName());
        s.getCourses().stream()
                .sorted(Comparator.comparing(Course::getCode))
                .forEach(c -> System.out.println("   " + c));

        // (c) @EntityGraph: nạp course + students
        Course c = courseService.getWithStudents("SWP391");
        System.out.println("(c) " + c.getCode() + " - " + c.getName());
        c.getStudents().stream()
                .sorted(Comparator.comparing(com.hsf302.ch4.pojo.Student::getFullName))
                .forEach(st -> System.out.println("   " + st));
    }

    private void todo17() {
        title("TODO 17: native query (courses with available seats)");
        printList("Available courses", courseService.findAvailableCourses());
    }

    private void todo18() {
        title("TODO 18: Custom Repository (Criteria API dynamic search)");
        printList("(a) Filter: name like 'data', minCredit=null, sem=null", 
                courseService.searchDynamic("data", null, null));
        printList("(b) Filter: name like 'm', minCredit=3, sem='FA26'", 
                courseService.searchDynamic("m", 3, "FA26"));
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