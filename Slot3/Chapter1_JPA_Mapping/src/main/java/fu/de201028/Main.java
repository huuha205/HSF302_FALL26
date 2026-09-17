import fu.de201028.dao.DepartmentDAO;
import fu.de201028.pojo.Department;
import fu.de201028.pojo.Employee;

public class Main {
    public static void main(String[] args) {

        DepartmentDAO deptDAO = new DepartmentDAO();

        Department dept = deptDAO.findByIdWithEmployees(2L);

        System.out.println("Department: " + dept.getName());

        for (Employee e : dept.getEmployees()) {
            System.out.println(
                    e.getId() + " - " + e.getFullName()
            );
        }
    }
}