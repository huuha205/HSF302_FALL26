package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import com.hsf302.ch4.pojo.Student;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public long count() {
        return courseRepository.count();
    }

    @Override
    public List<Course> findAllOrderByCode() {
        return courseRepository.findAll(Sort.by("code"));
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Override
    public List<Course> findBySemester(String semester) {
        return courseRepository.findBySemesterOrderByCodeAsc(semester);
    }

    @Override
    public long countBySemester(String semester) {
        return courseRepository.countBySemester(semester);
    }

    @Override
    public List<Course> findCoursesOfStudent(String studentCode) {
        return courseRepository.findByStudents_StudentCodeOrderByCodeAsc(studentCode);
    }

    @Override
    public List<Course> findCoursesOfDepartment(String deptCode, boolean distinct) {
        return distinct
                ? courseRepository.findDistinctByStudents_Department_CodeOrderByCodeAsc(deptCode)
                : courseRepository.findByStudents_Department_CodeOrderByCodeAsc(deptCode);
    }

    @Override
    public List<Course> findCoursesWithoutStudents() {
        return courseRepository.findByStudentsIsEmpty();
    }

    @Override
    public List<com.hsf302.ch4.dto.CourseStatDTO> getStatistics() {
        return courseRepository.getCourseStats();
    }

    @Override
    public List<Course> findFullCourses() {
        return courseRepository.findFullCourses();
    }

    @Override
    public Course getWithStudents(String code) {
        return courseRepository.findWithStudentsByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
    }

    @Override
    public List<Course> findAvailableCourses() {
        return courseRepository.findCoursesWithAvailableSeats();
    }

    @Override
    public List<Course> searchDynamic(String namePart, Integer minCredits, String semester) {
        return courseRepository.findCoursesByDynamicFilter(namePart, minCredits, semester);
    }

    @Override
    @Transactional
    public void deleteCourseDirectly(String code) {
        Course c = courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
        courseRepository.delete(c);
        courseRepository.flush(); // To trigger ConstraintViolationException
    }

    @Override
    @Transactional
    public int deleteCourse(String code) {
        Course c = courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));

        // Create a copy to avoid ConcurrentModificationException since unenroll modifies the set
        List<Student> students = new ArrayList<>(c.getStudents());
        int removedCount = 0;
        for (Student s : students) {
            s.unenroll(c);
            removedCount++;
        }

        courseRepository.delete(c);
        return removedCount;
    }
}