package com.hsf302.chapter6.service.impl;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.entity.Major;
import com.hsf302.chapter6.repository.StudentRepository;
import com.hsf302.chapter6.repository.MajorRepository;
import com.hsf302.chapter6.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)          // mặc định: mọi method chỉ đọc
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;

    public StudentServiceImpl(StudentRepository studentRepository, MajorRepository majorRepository) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    public Page<Student> findAll(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword.trim(), keyword.trim(), pageable);
        }
        return studentRepository.findAll(pageable);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional                      // ghi dữ liệu → bỏ readOnly
    public Student create(StudentForm form) {
        Student student = new Student();
        student.setName(form.getName());
        student.setEmail(form.getEmail());
        student.setAge(form.getAge());
        
        Major major = majorRepository.findById(form.getMajorId()).orElseThrow(() -> new IllegalArgumentException("Invalid major Id:" + form.getMajorId()));
        student.setMajor(major);
        
        student.setGpa(form.getGpa());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public boolean update(Long id, StudentForm form) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setName(form.getName());
                    existing.setEmail(form.getEmail());
                    existing.setAge(form.getAge());
                    
                    Major major = majorRepository.findById(form.getMajorId()).orElseThrow(() -> new IllegalArgumentException("Invalid major Id:" + form.getMajorId()));
                    existing.setMajor(major);
                    
                    existing.setGpa(form.getGpa());
                    // Không cần gọi save(): entity đang "managed",
                    // Hibernate tự sinh UPDATE khi transaction commit (dirty checking)
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean isEmailTaken(String email, Long excludeId) {
        if (email == null || email.isBlank()) return false;
        return excludeId == null
                ? studentRepository.existsByEmailIgnoreCase(email.trim())
                : studentRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), excludeId);
    }

    @Override
    public List<Major> getMajors() {
        return majorRepository.findAll();
    }
}
