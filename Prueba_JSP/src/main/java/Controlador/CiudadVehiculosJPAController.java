package Controlador;

import Modelo.Tb_CiudadVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad Tb_CiudadVehiculo. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos Tb_CiudadVehiculo en la base de datos.
 */
public class CiudadVehiculosJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public CiudadVehiculosJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    /**
     * Obtiene una instancia de EntityManager.
     *
     * @return EntityManager creado a partir de la fábrica de entidades.
     */
    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    /**
     * Crea y persiste un nuevo Tb_CiudadVehiculo en la base de datos.
     *
     * @param ciudadVehiculo El objeto Tb_CiudadVehiculo que se desea crear.
     */
    public void create(Tb_CiudadVehiculo ciudadVehiculo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ciudadVehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un Tb_CiudadVehiculo existente en la base de datos.
     *
     * @param ciudadVehiculo El objeto Tb_CiudadVehiculo que se desea editar.
     * @throws Exception si ocurre un error al editar o si el Tb_CiudadVehiculo no existe.
     */
    public void edit(Tb_CiudadVehiculo ciudadVehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ciudadVehiculo = em.merge(ciudadVehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = ciudadVehiculo.getId();
                if (findCiudadVehiculo(id) == null) {
                    throw new Exception("El ciudadVehiculo con id " + id + " ya no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina un Tb_CiudadVehiculo de la base de datos.
     *
     * @param id El ID del Tb_CiudadVehiculo a eliminar.
     * @throws PersistenceException si el Tb_CiudadVehiculo está en uso y no se puede eliminar.
     * @throws Exception si el Tb_CiudadVehiculo no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tb_CiudadVehiculo ciudadVehiculo;
            try {
                ciudadVehiculo = em.getReference(Tb_CiudadVehiculo.class, id);
                ciudadVehiculo.getId();  // Verificar que la ciudad existe
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La ciudad con id " + id + " ya no existe.", enfe);
            }

            em.remove(ciudadVehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Verificar si la excepción es de tipo ConstraintViolationException
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar la ciudad porque está en uso.", e);
            }
            throw e;  // Propagar cualquier otra PersistenceException
        } catch (Exception e) {
            // Capturar cualquier otra excepción inesperada
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Hacer rollback si ocurre un error
            }
            throw e;  // Propagar la excepción hacia la capa superior
        } finally {
            if (em != null) {
                em.close();  // Cerrar el EntityManager
            }
        }
    }

    /**
     * Obtiene una lista de todos los Tb_CiudadVehiculo.
     *
     * @return Lista de todos los Tb_CiudadVehiculo.
     */
    public List<Tb_CiudadVehiculo> findCiudadVehiculoEntities() {
        return findCiudadVehiculoEntities(true, -1, -1);
    }

    /**
     * Obtiene una lista de Tb_CiudadVehiculo con límites de resultados.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de Tb_CiudadVehiculo en el rango especificado.
     */
    public List<Tb_CiudadVehiculo> findCiudadVehiculoEntities(int maxResults, int firstResult) {
        return findCiudadVehiculoEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un Tb_CiudadVehiculo por su ID.
     *
     * @param id ID del Tb_CiudadVehiculo a buscar.
     * @return Tb_CiudadVehiculo con el ID especificado o null si no se encuentra.
     */
    public Tb_CiudadVehiculo findCiudadVehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tb_CiudadVehiculo.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método privado para obtener Tb_CiudadVehiculo con límites de resultados.
     *
     * @param all Si es true, devuelve todos los resultados.
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de Tb_CiudadVehiculo en el rango especificado o todos si all es true.
     */
    private List<Tb_CiudadVehiculo> findCiudadVehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tb_CiudadVehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
