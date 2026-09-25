package fu.de201028;

import fu.de201028.dao.EmployeeDAO;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO dao = new EmployeeDAO();

        // TODO 5.11
        dao.deactivateEmployee(20002L);

        System.out.println("Deactivate successfully!");
    }
}