package Servlets;


import Controlador.PersistenciaController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ActualizarBD {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        PersistenciaController controller = new PersistenciaController();

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            entityManager = entityManagerFactory.createEntityManager();

            System.out.println("EntityManagerFactory creado correctamente. Conexión a la base de datos exitosa.");

            // Aquí puedes realizar operaciones de persistencia utilizando EntityManager
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }
    }
}
