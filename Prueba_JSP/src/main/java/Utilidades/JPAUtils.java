package Utilidades;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para gestionar la fábrica de entidades (EntityManagerFactory) en JPA.
 * Implementa el patrón Singleton para mantener una única instancia de EntityManagerFactory.
 */
public class JPAUtils {

    // Instancia única de EntityManagerFactory, inicializada de manera estática.
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    /**
     * Constructor privado para evitar la creación de instancias de la clase.
     * La clase es completamente estática y utilitaria.
     */
    private JPAUtils() {}

    /**
     * Obtiene la instancia única de EntityManagerFactory.
     * Esta instancia se crea al cargar la clase por primera vez y se mantiene activa
     * durante el ciclo de vida de la aplicación.
     *
     * @return Instancia única de EntityManagerFactory.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
