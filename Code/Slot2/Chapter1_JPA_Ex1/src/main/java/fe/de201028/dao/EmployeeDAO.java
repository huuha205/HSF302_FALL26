package fe.de201028.dao;

import fe.de201028.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmployeeDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302PU");

    public void save(Employee e) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(e);

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