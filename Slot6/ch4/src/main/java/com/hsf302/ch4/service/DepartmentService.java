package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.DepartmentStatDTO;
import com.hsf302.ch4.pojo.Department;

import java.util.List;

public interface DepartmentService {
    // Các method được bổ sung dần từ TODO 6
    long count();

    boolean existsById(Long id);

    List<Department> findDepartmentsWithoutStudents();

    List<DepartmentStatDTO> getStatistics();
}