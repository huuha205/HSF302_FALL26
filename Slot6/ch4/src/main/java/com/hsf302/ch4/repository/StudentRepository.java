package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {
    // sẽ bổ sung dần ở các TODO sau
    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    long countByActiveTrue();

    List<Student> findByFullNameContainingIgnoreCase(String keyword);

    List<Student> findByEmailEndingWith(String suffix);

    List<Student> findByEmailIsNull();

    List<Student> findByGpaBetweenOrderByGpaDesc(double min, double max);

    List<Student> findByGenderAndActiveTrue(Gender gender);

    List<Student> findByDobAfter(LocalDate date);
}