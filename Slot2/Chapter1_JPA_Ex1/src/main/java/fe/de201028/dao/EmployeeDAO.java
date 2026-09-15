package fe.de201028.dao;

import fe.de201028.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import fe.de201028.pojo.Gender;
import java.math.BigDecimal;

public class EmployeeDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302PU");

    public void save(Employee e) {

        EntityManager em = emf.createEntityManager();

        try {
            // Transient: e is a new object and is not managed by JPA yet.

            em.getTransaction().begin();

            // Transient -> Managed
            em.persist(e);

            // JPA will insert the Employee into the database when the transaction is committed.
            em.getTransaction().commit();

            // After commit, e remains Managed while it is associated with this EntityManager.

        } catch (RuntimeException ex) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw ex;

        } finally {
            // Managed -> Detached when EntityManager is closed.
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

    public List<Employee> findByGender(Gender gender) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Employee e WHERE e.gender = :gender",
                            Employee.class
                    )
                    .setParameter("gender", gender)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Employee> findBySalaryGreaterThan(BigDecimal salary) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Employee e WHERE e.salary > :salary",
                            Employee.class
                    )
                    .setParameter("salary", salary)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public Employee update(Employee e) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee updated = em.merge(e);

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

            Employee e = em.find(Employee.class, id);

            if (e != null) {
                em.remove(e);
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

}