package fu.de201028;

import fu.de201028.dao.EmployeeDAO;
import fu.de201028.pojo.Department;
import fu.de201028.pojo.Employee;
import fu.de201028.pojo.Gender;
import fu.de201028.pojo.Project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hsf302FU");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Lấy Department có sẵn
            Department department = em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class
            ).setMaxResults(1).getSingleResult();

            // TODO 5.7 - Tạo 3 Employee
            Employee e1 = new Employee(
                    "nv1@gmail.com",
                    "Nguyen Van A",
                    Gender.MALE,
                    new BigDecimal("1500"),
                    LocalDate.of(2024, 1, 10)
            );

            Employee e2 = new Employee(
                    "nv2@gmail.com",
                    "Nguyen Van B",
                    Gender.FEMALE,
                    new BigDecimal("1800"),
                    LocalDate.of(2024, 2, 15)
            );

            Employee e3 = new Employee(
                    "nv3@gmail.com",
                    "Nguyen Van C",
                    Gender.OTHER,
                    new BigDecimal("2000"),
                    LocalDate.of(2024, 3, 20)
            );

            e1.setDepartment(department);
            e2.setDepartment(department);
            e3.setDepartment(department);

            // TODO 5.7 - Tạo 2 Project
            Project p1 = new Project();
            p1.setProjectCode("P001");
            p1.setProjectName("Project A");
            p1.setBudget(new BigDecimal("50000"));
            p1.setStartDate(LocalDate.of(2026, 1, 1));
            p1.setEndDate(LocalDate.of(2026, 6, 30));

            Project p2 = new Project();
            p2.setProjectCode("P002");
            p2.setProjectName("Project B");
            p2.setBudget(new BigDecimal("70000"));
            p2.setStartDate(LocalDate.of(2026, 2, 1));
            p2.setEndDate(LocalDate.of(2026, 8, 31));

            // Lưu Employee và Project
            em.persist(e1);
            em.persist(e2);
            em.persist(e3);

            em.persist(p1);
            em.persist(p2);

            tx.commit();

            // TODO 5.6 - Phân công Employee vào Project
            EmployeeDAO dao = new EmployeeDAO();

            dao.assignEmployeeToProject(e1.getId(), p1.getId());
            dao.assignEmployeeToProject(e1.getId(), p2.getId());

            dao.assignEmployeeToProject(e2.getId(), p2.getId());

            dao.assignEmployeeToProject(e3.getId(), p1.getId());

            // In kết quả
            System.out.println("=== EMPLOYEE - PROJECT ===");

            EntityManager em2 = emf.createEntityManager();

            List<Employee> employees = em2.createQuery(
                    "SELECT DISTINCT e FROM Employee e " +
                            "LEFT JOIN FETCH e.projects",
                    Employee.class
            ).getResultList();

            for (Employee e : employees) {
                System.out.println(
                        e.getFullName() + " -> " + e.getProjects()
                );

                for (Project p : e.getProjects()) {
                    System.out.println(
                            "   " + p.getProjectCode()
                                    + " - " + p.getProjectName()
                    );
                }
            }

            em2.close();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}