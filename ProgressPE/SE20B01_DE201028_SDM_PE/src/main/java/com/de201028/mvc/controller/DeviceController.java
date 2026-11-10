package com.de201028.mvc.controller;

import com.de201028.mvc.dto.DeviceDTO;
import com.de201028.mvc.entity.Device;
import com.de201028.mvc.service.CategoryService;
import com.de201028.mvc.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeviceController {

    private final DeviceService deviceService;
    private final CategoryService categoryService;

    public DeviceController(DeviceService deviceService, CategoryService categoryService) {
        this.deviceService = deviceService;
        this.categoryService = categoryService;
    }

    // Q1: Danh sách thiết bị & Tìm kiếm (Q5.4: Mapping gốc bắt buộc là /)
    @GetMapping("/")
    public String listDevices(@RequestParam(value = "deviceName", required = false) String deviceName,
                              Model model) {
        if (deviceName != null && !deviceName.trim().isEmpty()) {
            model.addAttribute("devices", deviceService.searchDevices(deviceName));
            model.addAttribute("deviceName", deviceName);
        } else {
            model.addAttribute("devices", deviceService.getAllDevices());
            model.addAttribute("deviceName", "");
        }
        return "device-list";
    }

    // Q3.1: Hiển thị form Add New Device
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("deviceDTO", new DeviceDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "device-add";
    }

    // Q3.2: Xử lý lưu thiết bị mới + Validation
    @PostMapping("/add")
    public String saveDevice(@Valid @ModelAttribute("deviceDTO") DeviceDTO deviceDTO,
                             BindingResult bindingResult,
                             Model model) {
        // Thực hiện kiểm tra các rule nghiệp vụ (trùng mã, trim tên, ngày tương lai...)
        deviceService.validateDeviceDTO(deviceDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            // Khi có lỗi: giữ nguyên dữ liệu đã nhập trên form và load lại danh mục
            model.addAttribute("categories", categoryService.getAllCategories());
            return "device-add";
        }

        // Khi hợp lệ: lưu vào CSDL
        deviceService.createDevice(deviceDTO);

        // Q3.2a: Hiện thông báo thành công và xóa trắng các trường nhập liệu
        model.addAttribute("successMessage", "Created new device successfully");
        model.addAttribute("deviceDTO", new DeviceDTO()); // Reset form
        model.addAttribute("categories", categoryService.getAllCategories());
        return "device-add";
    }

    // Q2: Xóa thiết bị (hỗ trợ cả GET và POST)
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteDevice(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            deviceService.deleteDevice(id);
            // Q2.2 / 3.3: Dùng FlashAttribute để thông báo chỉ hiển thị đúng 1 lần sau khi redirect
            redirectAttributes.addFlashAttribute("successMessage", "Deleted successfully");
        } catch (IllegalStateException e) {
            // Khi thiết bị đang là Active (AC) -> hiển thị thông báo lỗi
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }

    // Q4: Xem chi tiết thiết bị
    @GetMapping("/view/{id}")
    public String viewDevice(@PathVariable("id") Integer id, Model model) {
        Device device = deviceService.getDeviceById(id);
        if (device == null) {
            throw new IllegalArgumentException("Device not found with id: " + id);
        }
        model.addAttribute("device", device);
        return "device-view";
    }
}