package Controlador;



import Modelo.TbTipovehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;


/**
 * Controlador JPA para la entidad TbTipovehiculo. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbTipovehiculo en la base de datos.
 */
public class TiposVehiculosJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public TiposVehiculosJPAController() {
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
     * Crea y persiste un nuevo TbTipovehiculo en la base de datos.
     *
     * @param tipovehiculo El objeto TbTipovehiculo que se desea crear.
     * @throws Exception si ocurre un error durante la persistencia.
     */
    public void create(TbTipovehiculo tipovehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipovehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al persistir la entidad", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbTipovehiculo existente en la base de datos.
     *
     * @param tipovehiculo El objeto TbTipovehiculo que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbTipovehiculo no existe.
     */
    public void edit(TbTipovehiculo tipovehiculo) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipovehiculo = em.merge(tipovehiculo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = tipovehiculo.getId();
                if (findtipovehiculo(id) == null) {
                    throw new Exception("El tipo de vehículo con id " + id + " ya no existe.");
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
     * Elimina un TbTipovehiculo de la base de datos.
     *
     * @param id El ID del TbTipovehiculo a eliminar.
     * @throws PersistenceException si el TbTipovehiculo está en uso y no se puede eliminar.
     * @throws Exception si el TbTipovehiculo no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipovehiculo tipovehiculo;
            try {
                tipovehiculo = em.getReference(TbTipovehiculo.class, id);
                tipovehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El tipo de vehículo con id " + id + " ya no existe.", enfe);
            }
            em.remove(tipovehiculo);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el tipo de vehículo porque está en uso.", e);
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra todas las entidades TbTipovehiculo.
     *
     * @return Lista de todas las entidades TbTipovehiculo.
     */
    public List<TbTipovehiculo> findtipovehiculoEntities() {
        return findtipovehiculoEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades TbTipovehiculo.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de entidades TbTipovehiculo en el rango especificado.
     */
    public List<TbTipovehiculo> findtipovehiculoEntities(int maxResults, int firstResult) {
        return findtipovehiculoEntities(false, maxResults, firstResult);
    }

    /**
     * Método privado para encontrar entidades TbTipovehiculo, con la opción de devolver todas o aplicar paginación.
     *
     * @param all Si es true, devuelve todas las entidades; si es false, aplica la paginación.
     * @param maxResults Número máximo de resultados a devolver cuando se aplica paginación.
     * @param firstResult Primer resultado a devolver cuando se aplica paginación.
     * @return Lista de entidades TbTipovehiculo según el criterio especificado.
     */
    private List<TbTipovehiculo> findtipovehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbTipovehiculo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Encuentra un TbTipovehiculo por su ID.
     *
     * @param id ID del TbTipovehiculo a buscar.
     * @return TbTipovehiculo con el ID especificado o null si no se encuentra.
     */
    public TbTipovehiculo findtipovehiculo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbTipovehiculo.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
