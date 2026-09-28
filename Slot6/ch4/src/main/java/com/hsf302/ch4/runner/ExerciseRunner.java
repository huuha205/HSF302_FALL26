package com.hsf302.ch4.runner;

import com.hsf302.ch4.service.DepartmentService;
import com.hsf302.ch4.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;
import java.util.Collection;
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class ExerciseRunner implements CommandLineRunner {

    private final DepartmentService departmentService;
    private final StudentService studentService;

    @Override

    public void run(String... args) {
        todo6();
        todo7();
        todo8();
        todo9();
    }

    private void todo6() {
        title("TODO 6: count / findById / existsById");

        System.out.println("Departments: " + departmentService.count());
        System.out.println("Students   : " + studentService.count());

        studentService.findById(1L).ifPresentOrElse(
                s -> System.out.println("findById(1) -> " + s),
                () -> System.out.println("findById(1) -> Not found")
        );

        System.out.println("findById(99) -> " +
                studentService.findById(99L)
                        .map(Object::toString)
                        .orElse("Not found"));

        System.out.println("existsById(4) department -> " +
                departmentService.existsById(4L));
    }

    private void title(String t) {
        System.out.println("\n===== " + t + " =====");
    }

    private void todo7() {
        title("TODO 7: Sort & Pageable");

        printList(
                "All students order by GPA desc",
                studentService.findAllOrderByGpaDesc()
        );

        Page<Student> page =
                studentService.findPage(1, 3, "fullName");

        printList(
                "Page index " + page.getNumber()
                        + " (size " + page.getSize() + ")",
                page.getContent()
        );

        System.out.println(
                "totalElements=" + page.getTotalElements()
                        + ", totalPages=" + page.getTotalPages()
                        + ", hasNext=" + page.hasNext()
                        + ", hasPrevious=" + page.hasPrevious()
        );
    }

    private void printList(String label, Collection<?> list) {
        System.out.println("-- " + label + ":");
        list.forEach(o -> System.out.println("   " + o));
        System.out.println("   -> " + list.size() + " record(s)");
    }

    private void todo8() {
        title("TODO 8: findBy / existsBy / countBy");

        for (String code : List.of("AI002", "XX999")) {
            System.out.println("findByStudentCode(" + code + ") -> " +
                    studentService.findByStudentCode(code)
                            .map(Object::toString)
                            .orElse("Not found"));
        }

        System.out.println("isEmailExisted(binh.tt@fpt.edu.vn) -> "
                + studentService.isEmailExisted("binh.tt@fpt.edu.vn"));

        System.out.println("countActive -> " + studentService.countActive());
    }

    private void todo9() {
        title("TODO 9: Containing / EndingWith / IsNull");

        printList(
                "fullName contains 'nguyen'",
                studentService.searchByName("nguyen")
        );

        printList(
                "email domain 'gmail.com'",
                studentService.findByEmailDomain("gmail.com")
        );

        printList(
                "email is null",
                studentService.findWithoutEmail()
        );
    }


}