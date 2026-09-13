package fe.de201028;

import fe.de201028.dao.EmployeeDAO;
import fe.de201028.pojo.Employee;
import fe.de201028.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO dao = new EmployeeDAO();

        // 1. Create
        Employee e = new Employee();
        e.setFullName("Nguyen Van A");
        e.setEmail("nguyenvana@gmail.com");
        e.setSalary(new BigDecimal("15000000"));
        e.setGender(Gender.MALE);
        e.setHireDate(LocalDate.of(2023, 1, 10));
        e.setActive(true);

        dao.save(e);

        System.out.println("Created employee ID: " + e.getId());

        // 2. Read by ID
        Employee found = dao.findById(e.getId());

        if (found != null) {
            System.out.println("Found: " + found.getFullName());
        }

        // 3. Read all
        System.out.println("All employees:");

        for (Employee employee : dao.findAll()) {
            System.out.println(
                    employee.getId() + " - "
                            + employee.getFullName()
            );
        }

        // 4. Update
        found.setSalary(new BigDecimal("18000000"));

        Employee updated = dao.update(found);

        System.out.println(
                "Updated salary: " + updated.getSalary()
        );

        // 5. Conditional query
        System.out.println("Male employees:");

        for (Employee employee : dao.findByGender(Gender.MALE)) {
            System.out.println(employee.getFullName());
        }

        // 6. Delete
        dao.delete(e.getId());

        System.out.println("Deleted employee ID: " + e.getId());
    }
}