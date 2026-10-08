package com.hsf302.chapter6.repository;

import com.hsf302.chapter6.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /** Email đã tồn tại? (dùng khi thêm mới) */
    boolean existsByEmailIgnoreCase(String email);

    /** Email đã được sinh viên KHÁC dùng? (dùng khi cập nhật) */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    /** Tìm kiếm theo tên hoặc email */
    List<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, org.springframework.data.domain.Sort sort);
}
