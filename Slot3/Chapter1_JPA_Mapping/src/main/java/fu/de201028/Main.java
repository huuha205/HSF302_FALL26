package fu.de201028;

import fu.de201028.dao.DepartmentDAO;
import fu.de201028.pojo.Department;
import fu.de201028.pojo.Employee;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== FIX N+1 QUERY ===");

        DepartmentDAO dao = new DepartmentDAO();

        List<Department> departments = dao.findAllWithEmployees();

        for (Department d : departments) {
            System.out.println(
                    d.getName()
                            + " - Employees: "
                            + d.getEmployees().size()
            );

            for (Employee e : d.getEmployees()) {
                System.out.println("  " + e.getId() + " - " + e.getFullName());
            }
        }
    }
}