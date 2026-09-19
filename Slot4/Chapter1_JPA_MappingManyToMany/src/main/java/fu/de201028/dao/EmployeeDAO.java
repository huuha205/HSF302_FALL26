package fu.de201028.dao;

import jakarta.persistence.EntityTransaction;
import fu.de201028.pojo.Project;

import fu.de201028.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class EmployeeDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302FU");

    public void save(Employee employee) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(employee);

            em.getTransaction().commit();

        } catch (RuntimeException ex) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    public List<Employee> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM Employee e",
                    Employee.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    public Employee findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Employee.class, id);

        } finally {
            em.close();
        }
    }

    public Employee update(Employee employee) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee updated = em.merge(employee);

            em.getTransaction().commit();

            return updated;

        } catch (RuntimeException ex) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, id);

            if (employee != null) {
                em.remove(employee);
            }

            em.getTransaction().commit();

        } catch (RuntimeException ex) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw ex;

        } finally {
            em.close();
        }
    }

    // TODO 5.6
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            employee.assignToProject(project);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    // TODO 5.8
    public void countActiveEmployeesAndSumSalaryByProject() {
        EntityManager em = emf.createEntityManager();

        try {
            List<Object[]> results = em.createQuery(
                    "SELECT p.projectName, COUNT(e), SUM(e.salary) " +
                            "FROM Project p JOIN p.employees e " +
                            "WHERE e.active = true " +
                            "GROUP BY p.projectName",
                    Object[].class
            ).getResultList();

            for (Object[] row : results) {
                System.out.println(
                        "Project: " + row[0]
                                + " | Active employees: " + row[1]
                                + " | Total salary: " + row[2]
                );
            }

        } finally {
            em.close();
        }
    }


}