package Controlador;


import Modelo.TbRegistro;
import Modelo.TbReportes;
import Modelo.TbVehiculo;
import Utilidades.JPAUtils;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

public class ReportesJPAController {

    private EntityManagerFactory fabricaEntidades;

    public ReportesJPAController() {
        this.fabricaEntidades = JPAUtils.getEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }


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
                    throw new Exception("The reporte with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

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
                throw new Exception("The reporte with id " + id + " no longer exists.", enfe);
            }
            em.remove(reporte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public List<TbReportes> findTbReportesEntities() {
        return findTbReportesEntities(true, -1, -1);
    }

    public List<TbReportes> findTbReportesEntities(int maxResults, int firstResult) {
        return findTbReportesEntities(false, maxResults, firstResult);
    }
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

    public List<TbReportes> findReportesGestor(int idGestor) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbReportes> query = em.createQuery(
                    "SELECT r FROM tb_reportes r WHERE r.gestor.id = :idGestor",
                    TbReportes.class
            );
            query.setParameter("idGestor", idGestor);
            return query.getResultList();
        }
        finally {
            em.close();
        }
    }


    // Consulta con Criteria API para registros por el ID del Aprendiz
    public List<TbReportes> findReportesAprendiz(int idAprendiz) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TbReportes> cq = cb.createQuery(TbReportes.class);

            Root<TbReportes> root = cq.from(TbReportes.class);

            // JOIN FETCH de vehiculo y persona para inicializar todo
            Fetch<TbReportes, TbVehiculo> vehiculoFetch = root.fetch("vehiculo", JoinType.INNER);
            vehiculoFetch.fetch("persona", JoinType.INNER);  // Para cargar la persona a través del vehículo
            root.fetch("gestor", JoinType.INNER);

            // Añadir el where para filtrar por id del aprendiz (persona)
            cq.select(root).where(cb.equal(root.get("vehiculo").get("persona").get("id"), idAprendiz));

            TypedQuery<TbReportes> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}


