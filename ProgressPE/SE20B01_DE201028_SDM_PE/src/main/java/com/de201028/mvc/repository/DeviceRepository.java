package com.de201028.mvc.repository;

import com.de201028.mvc.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    // Q1.1: Sắp xếp theo Installation Date giảm dần (Descending)
    List<Device> findAllByOrderByInstallationDateDesc();

    // Q1.2: Tìm kiếm theo Device Name chứa từ khóa (partial match) + sắp xếp theo Installation Date giảm dần
    List<Device> findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(String deviceName);

    // Q3: Kiểm tra trùng mã Device Code khi thêm mới
    boolean existsByDeviceCode(String deviceCode);
}