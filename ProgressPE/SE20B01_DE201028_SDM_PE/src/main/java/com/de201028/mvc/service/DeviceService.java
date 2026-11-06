package com.de201028.mvc.service;

import com.de201028.mvc.dto.DeviceDTO;
import com.de201028.mvc.entity.Device;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();
    List<Device> searchDevices(String keyword);
    Device getDeviceById(Integer id);
    void validateDeviceDTO(DeviceDTO dto, BindingResult bindingResult);
    void createDevice(DeviceDTO dto);
    void deleteDevice(Integer id);
}