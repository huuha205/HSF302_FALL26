package com.de201028.mvc.service.impl;

import com.de201028.mvc.dto.DeviceDTO;
import com.de201028.mvc.entity.Category;
import com.de201028.mvc.entity.Device;
import com.de201028.mvc.repository.CategoryRepository;
import com.de201028.mvc.repository.DeviceRepository;
import com.de201028.mvc.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DeviceRepository deviceRepository;
    private final CategoryRepository categoryRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, CategoryRepository categoryRepository) {
        this.deviceRepository = deviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Device> getAllDevices() {
        // Q1.1: Sắp xếp Installation Date giảm dần
        return deviceRepository.findAllByOrderByInstallationDateDesc();
    }

    @Override
    public List<Device> searchDevices(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllDevices();
        }
        // Q1.2: Wildcard search + loại bỏ khoảng trắng 2 đầu + sort Installation Date Desc
        return deviceRepository.findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(keyword.trim());
    }

    @Override
    public Device getDeviceById(Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public void validateDeviceDTO(DeviceDTO dto, BindingResult bindingResult) {
        // 1. Kiểm tra Device Code không được trùng lặp
        if (dto.getDeviceCode() != null && !dto.getDeviceCode().trim().isEmpty()) {
            if (deviceRepository.existsByDeviceCode(dto.getDeviceCode().trim())) {
                bindingResult.addError(new FieldError("deviceDTO", "deviceCode", "Device Code already exists"));
            }
        }

        // 2. Kiểm tra Device Name không được chứa khoảng trắng ở 2 đầu (trim)
        if (dto.getDeviceName() != null && !dto.getDeviceName().isEmpty()) {
            if (!dto.getDeviceName().equals(dto.getDeviceName().trim())) {
                bindingResult.addError(new FieldError("deviceDTO", "deviceName", "Device Name must not contain leading or trailing spaces"));
            }
        }

        // 3. Kiểm tra Category tồn tại trong Database
        if (dto.getCategoryId() != null) {
            if (!categoryRepository.existsById(dto.getCategoryId())) {
                bindingResult.addError(new FieldError("deviceDTO", "categoryId", "Selected Category does not exist"));
            }
        }

        // 4. Kiểm tra Installation Date đúng định dạng dd/MM/yyyy và phải là ngày trong tương lai (future date)
        if (dto.getInstallationDate() != null && !dto.getInstallationDate().trim().isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(dto.getInstallationDate().trim(), DATE_FORMATTER);
                if (!date.isAfter(LocalDate.now())) {
                    bindingResult.addError(new FieldError("deviceDTO", "installationDate", "Installation Date must be a future date"));
                }
            } catch (DateTimeParseException e) {
                bindingResult.addError(new FieldError("deviceDTO", "installationDate", "Installation Date must follow dd/MM/yyyy format"));
            }
        }
    }

    @Override
    public void createDevice(DeviceDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId()));

        LocalDate date = LocalDate.parse(dto.getInstallationDate().trim(), DATE_FORMATTER);

        Device device = new Device();
        device.setDeviceCode(dto.getDeviceCode().trim());
        device.setDeviceName(dto.getDeviceName().trim());
        device.setCategory(category);
        device.setPrice(dto.getPrice());
        device.setWarrantyMonths(dto.getWarrantyMonths());
        device.setInstallationDate(date);
        device.setStatus(dto.getStatus());

        deviceRepository.save(device);
    }

    @Override
    public void deleteDevice(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + id));

        // Q2.2: Nếu status là Active (AC) thì chặn xóa và ném ngoại lệ
        if ("AC".equalsIgnoreCase(device.getStatus())) {
            throw new IllegalStateException("Cannot delete an active device.");
        }

        deviceRepository.delete(device);
    }
}