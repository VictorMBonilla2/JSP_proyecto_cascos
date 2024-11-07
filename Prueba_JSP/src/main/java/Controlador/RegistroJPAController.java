package Controlador;

import Modelo.TbRegistro;
import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

/**
 * Controlador JPA para la entidad TbRegistro. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbRegistro en la base de datos.
 */
public class RegistroJPAController {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public RegistroJPAController() {
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
     * Crea y persiste un nuevo TbRegistro en la base de datos.
     *
     * @param registro El objeto TbRegistro que se desea crear.
     */
    public void create(TbRegistro registro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(registro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbRegistro existente en la base de datos.
     *
     * @param registro El objeto TbRegistro que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbRegistro no existe.
     */
    public void edit(TbRegistro registro) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            registro = em.merge(registro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = registro.getIdRegistro();
                if (findTbRegistro(id) == null) {
                    throw new Exception("El registro con id " + id + " ya no existe.");
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
     * Elimina un TbRegistro de la base de datos.
     *
     * @param id El ID del TbRegistro a eliminar.
     * @throws Exception si el TbRegistro no existe o si ocurre un error durante la eliminación.
     */
    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbRegistro registro;
            try {
                registro = em.getReference(TbRegistro.class, id);
                registro.getIdRegistro();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El registro con id " + id + " ya no existe.", enfe);
            }
            em.remove(registro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra todas las entidades TbRegistro.
     *
     * @return Lista de todas las entidades TbRegistro.
     */
    public List<TbRegistro> findTbRegistroEntities() {
        return findTbRegistroEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades TbRegistro.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de entidades TbRegistro en el rango especificado.
     */
    public List<TbRegistro> findTbRegistroEntities(int maxResults, int firstResult) {
        return findTbRegistroEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un TbRegistro por su ID.
     *
     * @param id ID del TbRegistro a buscar.
     * @return TbRegistro con el ID especificado o null si no se encuentra.
     */
    public TbRegistro findTbRegistro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbRegistro.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<TbRegistro> findTbRegistroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbRegistro.class));
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
     * Encuentra una lista de registros asociados a un gestor específico, con soporte de paginación.
     *
     * @param idGestor ID del gestor.
     * @param dataInicio Índice del primer resultado.
     * @param tamanioPagina Tamaño de la página.
     * @return Lista de TbRegistro asociados al gestor especificado.
     */
    public List<TbRegistro> findRegistrosGestor(int idGestor, int dataInicio, int tamanioPagina) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbRegistro> query = em.createQuery(
                    "SELECT r FROM TbRegistro r WHERE r.gestor.id = :idGestor",
                    TbRegistro.class
            );
            query.setParameter("idGestor", idGestor);
            query.setFirstResult(dataInicio);
            query.setMaxResults(tamanioPagina);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Encuentra una lista de registros asociados a un aprendiz específico, con soporte de paginación.
     *
     * @param idAprendiz ID del aprendiz.
     * @param dataInicio Índice del primer resultado.
     * @param tamanioPagina Tamaño de la página.
     * @return Lista de TbRegistro asociados al aprendiz especificado.
     */
    public List<TbRegistro> findRegistrosAprendiz(int idAprendiz, int dataInicio, int tamanioPagina) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbRegistro> cq = cb.createQuery(TbRegistro.class);

            Root<TbRegistro> root = cq.from(TbRegistro.class);
            Fetch<TbRegistro, TbVehiculo> vehiculoFetch = root.fetch("vehiculo", JoinType.INNER);
            vehiculoFetch.fetch("persona", JoinType.INNER);
            root.fetch("gestor", JoinType.INNER);

            cq.select(root).where(cb.equal(root.get("vehiculo").get("persona").get("id"), idAprendiz));

            TypedQuery<TbRegistro> query = em.createQuery(cq);
            query.setFirstResult(dataInicio);
            query.setMaxResults(tamanioPagina);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número total de registros asociados a un gestor específico.
     *
     * @param idGestor ID del gestor.
     * @return Número total de registros asociados al gestor.
     */
    public long contarRegistrosGestor(int idGestor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r WHERE r.gestor.id = :idGestor",
                    Long.class
            );
            query.setParameter("idGestor", idGestor);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número total de registros asociados a un aprendiz específico.
     *
     * @param idAprendiz ID del aprendiz.
     * @return Número total de registros asociados al aprendiz.
     */
    public long contarRegistrosAprendiz(int idAprendiz) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r WHERE r.vehiculo.persona.id = :idAprendiz",
                    Long.class
            );
            query.setParameter("idAprendiz", idAprendiz);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número total de registros en la tabla TbRegistro.
     *
     * @return Número total de registros.
     */
    public long contarRegistros() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM TbRegistro r",
                    Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}

