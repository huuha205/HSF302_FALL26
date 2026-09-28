package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {
    // sẽ bổ sung dần ở các TODO sau
    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    long countByActiveTrue();
}