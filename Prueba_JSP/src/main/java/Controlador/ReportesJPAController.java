package Controlador;


import Modelo.TbRegistro;
import Modelo.TbReportes;
import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

/**
 * Controlador JPA para la entidad TbReportes. Proporciona operaciones CRUD
 * y métodos para la gestión de objetos TbReportes en la base de datos.
 */
public class ReportesJPAController {

    private EntityManagerFactory fabricaEntidades;

    /**
     * Constructor que inicializa el EntityManagerFactory.
     */
    public ReportesJPAController() {
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
     * Crea y persiste un nuevo TbReportes en la base de datos.
     *
     * @param reporte El objeto TbReportes que se desea crear.
     */
    public void create(TbReportes reporte) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Edita un TbReportes existente en la base de datos.
     *
     * @param reporte El objeto TbReportes que se desea editar.
     * @throws Exception si ocurre un error al editar o si el TbReportes no existe.
     */
    public void edit(TbReportes reporte) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reporte = em.merge(reporte);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = reporte.getId_reporte();
                if (findTbReportes(id) == null) {
                    throw new Exception("El reporte con id " + id + " ya no existe.");
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
     * Elimina un TbReportes de la base de datos.
     *
     * @param id El ID del TbReportes a eliminar.
     * @throws Exception si el TbReportes no existe o si ocurre un error durante la eliminación.
     */
    public void destroy(int id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TbReportes reporte;
            try {
                reporte = em.getReference(TbReportes.class, id);
                reporte.getId_reporte();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("El reporte con id " + id + " ya no existe.", enfe);
            }
            em.remove(reporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Encuentra todas las entidades TbReportes.
     *
     * @return Lista de todas las entidades TbReportes.
     */
    public List<TbReportes> findTbReportesEntities() {
        return findTbReportesEntities(true, -1, -1);
    }

    /**
     * Encuentra un rango de entidades TbReportes.
     *
     * @param maxResults Número máximo de resultados a devolver.
     * @param firstResult Primer resultado a devolver.
     * @return Lista de entidades TbReportes en el rango especificado.
     */
    public List<TbReportes> findTbReportesEntities(int maxResults, int firstResult) {
        return findTbReportesEntities(false, maxResults, firstResult);
    }

    /**
     * Encuentra un TbReportes por su ID.
     *
     * @param id ID del TbReportes a buscar.
     * @return TbReportes con el ID especificado o null si no se encuentra.
     */
    public TbReportes findTbReportes(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbReportes.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<TbReportes> findTbReportesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbReportes.class));
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
     * Encuentra una lista de reportes asociados a un gestor específico.
     *
     * @param idGestor ID del gestor.
     * @return Lista de TbReportes asociados al gestor especificado.
     */
    public List<TbReportes> findReportesGestor(int idGestor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbReportes> query = em.createQuery(
                    "SELECT r FROM tb_reportes r WHERE r.gestor.id = :idGestor",
                    TbReportes.class
            );
            query.setParameter("idGestor", idGestor);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Encuentra una lista de reportes asociados a un aprendiz específico.
     *
     * @param idAprendiz ID del aprendiz.
     * @return Lista de TbReportes asociados al aprendiz especificado.
     */
    public List<TbReportes> findReportesAprendiz(int idAprendiz) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbReportes> cq = cb.createQuery(TbReportes.class);

            Root<TbReportes> root = cq.from(TbReportes.class);
            Fetch<TbReportes, TbVehiculo> vehiculoFetch = root.fetch("vehiculo", JoinType.INNER);
            vehiculoFetch.fetch("persona", JoinType.INNER);
            root.fetch("gestor", JoinType.INNER);

            cq.select(root).where(cb.equal(root.get("vehiculo").get("persona").get("id"), idAprendiz));

            TypedQuery<TbReportes> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}



