package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Student;
import java.util.Optional;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {
    // Các method được bổ sung dần từ TODO 6
    long count();

    Optional<Student> findById(Long id);

    List<Student> findAllOrderByGpaDesc();

    Page<Student> findPage(int pageIndex, int size, String sortField);

    Optional<Student> findByStudentCode(String studentCode);

    boolean isEmailExisted(String email);

    long countActive();
}