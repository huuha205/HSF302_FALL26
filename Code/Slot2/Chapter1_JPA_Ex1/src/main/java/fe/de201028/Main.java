package fe.de201028;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hsf302PU");

        System.out.println("JPA started successfully!");

        emf.close();
    }
}