package com.hsf302.chapter6.service;

import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.entity.Major;
import com.hsf302.chapter6.dto.StudentForm;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Page<Student> findAll(String keyword, Pageable pageable);

    Optional<Student> findById(Long id);

    Student create(StudentForm form);

    /** @return true nếu tìm thấy và cập nhật; false nếu không tồn tại id */
    boolean update(Long id, StudentForm form);

    /** @return true nếu xoá được; false nếu không tồn tại id */
    boolean delete(Long id);

    /** Kiểm tra email trùng. excludeId = null khi thêm mới, = id hiện tại khi cập nhật */
    boolean isEmailTaken(String email, Long excludeId);

    List<Major> getMajors();
}
