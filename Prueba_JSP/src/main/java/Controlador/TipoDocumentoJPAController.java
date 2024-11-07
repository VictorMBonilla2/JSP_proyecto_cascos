package Controlador;

import Modelo.TbTipoDocumento;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

/**
 * Controlador JPA para la entidad TbTipoDocumento. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbTipoDocumento en la base de datos.
 */
public class TipoDocumentoJPAController implements Serializable {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public TipoDocumentoJPAController() {
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
     * Crea y persiste un nuevo TbTipoDocumento en la base de datos.
     *
     * @param tipoDocumento El objeto TbTipoDocumento que se desea crear.
     */
    public void create(TbTipoDocumento tipoDocumento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoDocumento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbTipoDocumento existente en la base de datos.
     *
     * @param tipoDocumento El objeto TbTipoDocumento que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbTipoDocumento no existe.
     */
    public void edit(TbTipoDocumento tipoDocumento) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoDocumento = em.merge(tipoDocumento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.isEmpty()) {
                int id = tipoDocumento.getId();
                if (findTbTipoDocumento(id) == null) {
                    throw new Exception("El tipo de documento con id " + id + " ya no existe.");
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
     * Elimina un TbTipoDocumento de la base de datos.
     *
     * @param id El ID del TbTipoDocumento a eliminar.
     * @throws PersistenceException si el TbTipoDocumento está en uso y no se puede eliminar.
     * @throws Exception si el TbTipoDocumento no existe o si ocurre cualquier otro error inesperado.
     */
    public void destroy(int id) throws PersistenceException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipoDocumento tipoDocumento;
            try {
                tipoDocumento = em.getReference(TbTipoDocumento.class, id);
                tipoDocumento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El tipo de documento con id " + id + " no existe.", enfe);
            }
            em.remove(tipoDocumento);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new PersistenceException("No se puede eliminar el tipo de documento porque está en uso.", e);
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
     * Encuentra un TbTipoDocumento por su ID.
     *
     * @param id ID del TbTipoDocumento a buscar.
     * @return TbTipoDocumento con el ID especificado o null si no se encuentra.
     */
    public TbTipoDocumento findTbTipoDocumento(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbTipoDocumento.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra todas las entidades TbTipoDocumento.
     *
     * @return Lista de todas las entidades TbTipoDocumento.
     */
    public List<TbTipoDocumento> findTbTipoDocumentoEntities() {
        return findTbTipoDocumentoEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades TbTipoDocumento.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de entidades TbTipoDocumento en el rango especificado.
     */
    public List<TbTipoDocumento> findTbTipoDocumentoEntities(int maxResults, int firstResult) {
        return findTbTipoDocumentoEntities(false, maxResults, firstResult);
    }

    /**
     * Método privado para encontrar entidades TbTipoDocumento, con la opción de devolver todas o aplicar paginación.
     *
     * @param all Si es true, devuelve todas las entidades; si es false, aplica la paginación.
     * @param maxResults Número máximo de resultados a devolver cuando se aplica paginación.
     * @param firstResult Primer resultado a devolver cuando se aplica paginación.
     * @return Lista de entidades TbTipoDocumento según el criterio especificado.
     */
    private List<TbTipoDocumento> findTbTipoDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbTipoDocumento.class));
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
}
