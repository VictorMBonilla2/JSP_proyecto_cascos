package Utilidades;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class    JPAUtils {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    private JPAUtils() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

}
