package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Course;
import java.util.List;

public interface CustomCourseRepository {
    List<Course> findCoursesByDynamicFilter(String namePart, Integer minCredits, String semester);
}
