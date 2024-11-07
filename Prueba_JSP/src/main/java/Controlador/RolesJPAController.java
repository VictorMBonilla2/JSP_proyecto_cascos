package Controlador;

import Modelo.Roles;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad Roles. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos Roles en la base de datos.
 */
public class RolesJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public RolesJPAController() {
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
     * Crea y persiste un nuevo Roles en la base de datos.
     *
     * @param rol El objeto Roles que se desea crear.
     */
    public void create(Roles rol) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un Roles existente en la base de datos.
     *
     * @param rol El objeto Roles que se desea editar.
     * @throws Exception si ocurre un error al editar o si el Roles no existe.
     */
    public void edit(Roles rol) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            rol = em.merge(rol);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = rol.getId();
                if (findRol(id) == null) {
                    throw new Exception("El rol con id " + id + " ya no existe.");
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
     * Elimina un Roles de la base de datos.
     *
     * @param id El ID del Roles a eliminar.
     * @throws PersistenceException si el Roles está en uso y no se puede eliminar.
     * @throws Exception si el Roles no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles rol;
            try {
                rol = em.getReference(Roles.class, id);
                rol.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El rol con id " + id + " ya no existe.", enfe);
            }

            em.remove(rol);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el rol porque está en uso.", e);
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
     * Encuentra todas las entidades Roles.
     *
     * @return Lista de todas las entidades Roles.
     */
    public List<Roles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades Roles.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de entidades Roles en el rango especificado.
     */
    public List<Roles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un Roles por su ID.
     *
     * @param id ID del Roles a buscar.
     * @return Roles con el ID especificado o null si no se encuentra.
     */
    public Roles findRol(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roles.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Método privado para encontrar entidades Roles, con la opción de devolver todas o aplicar paginación.
     *
     * @param all Si es true, devuelve todas las entidades; si es false, aplica la paginación.
     * @param maxResults Número máximo de resultados a devolver cuando se aplica paginación.
     * @param firstResult Primer resultado a devolver cuando se aplica paginación.
     * @return Lista de entidades Roles según el criterio especificado.
     */
    private List<Roles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roles.class));
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
