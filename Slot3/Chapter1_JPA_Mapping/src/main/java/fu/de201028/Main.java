import fu.de201028.dao.DepartmentDAO;

public class Main {

    public static void main(String[] args) {

        DepartmentDAO dao = new DepartmentDAO();

        dao.demonstrateNPlusOne();
    }
}