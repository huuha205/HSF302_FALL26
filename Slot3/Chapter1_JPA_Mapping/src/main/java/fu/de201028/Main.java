import fu.de201028.dao.DepartmentDAO;
import fu.de201028.pojo.Department;
import fu.de201028.pojo.Employee;
import fu.de201028.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Department dept = new Department();
        dept.setName("Sales");
        dept.setLocation("Da Nang");

        Employee emp = new Employee();
        emp.setEmail("sales1@company.com");
        emp.setFullName("Sales 1");
        emp.setGender(Gender.MALE);
        emp.setSalary(new BigDecimal("1000"));
        emp.setHireDate(LocalDate.now());

        Employee e2 = new Employee();
        e2.setEmail("sales2@company.com");
        e2.setFullName("Sales 2");
        e2.setGender(Gender.FEMALE);
        e2.setSalary(new BigDecimal("1200"));
        e2.setHireDate(LocalDate.now());

        Employee e3 = new Employee();
        e3.setEmail("sales3@company.com");
        e3.setFullName("Sales 3");
        e3.setGender(Gender.OTHER);
        e3.setSalary(new BigDecimal("1500"));
        e3.setHireDate(LocalDate.now());

        dept.addEmployee(emp);
        dept.addEmployee(e2);
        dept.addEmployee(e3);

        DepartmentDAO deptDAO = new DepartmentDAO();

        // Chỉ save Department
        deptDAO.save(dept);

        System.out.println("Department ID: " + dept.getId());
        System.out.println("Department: " + dept.getName());
        System.out.println("Employees: " + dept.getEmployees().size());
    }
}