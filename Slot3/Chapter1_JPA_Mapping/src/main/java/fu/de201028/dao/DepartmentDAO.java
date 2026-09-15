package fu.de201028.dao;

import fu.de201028.pojo.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class DepartmentDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302FU");

    public void save(Department department) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(department);

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

    public List<Department> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    public Department findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Department.class, id);

        } finally {
            em.close();
        }
    }

    public Department update(Department department) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Department updated = em.merge(department);

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

            Department department = em.find(Department.class, id);

            if (department != null) {
                em.remove(department);
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