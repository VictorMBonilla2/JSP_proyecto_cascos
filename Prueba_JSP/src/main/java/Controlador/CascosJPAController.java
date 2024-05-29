package Controlador;

import Modelo.TbCasco;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class CascosJPAController implements Serializable {

    public CascosJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }
    private EntityManagerFactory fabricaEntidades= null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbCasco casco) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(casco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbCasco casco) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            casco = em.merge(casco);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = casco.getId();
                if (findTbCasco(id) == null) {
                    throw new Exception("The espacio with id " + id + " no longer exists.");
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
            TbCasco casco;
            try {
                casco = em.getReference(TbCasco.class, id);
                casco.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The espacio with id " + id + " no longer exists.", enfe);
            }
            em.remove(casco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbCasco> findTbCascoEntities() {
        return findTbCascoEntities(true, -1, -1);
    }

    public List<TbCasco> findTbCascoEntities(int maxResults, int firstResult) {
        return findTbCascoEntities(false, maxResults, firstResult);
    }

    private List<TbCasco> findTbCascoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbCasco.class));
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

    public TbCasco findTbCasco(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbCasco.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public TbCasco buscarCascoPorPlaca(String placa) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<TbCasco> query = em.createQuery("SELECT c FROM TbCasco c WHERE c.placa_casco = :placa", TbCasco.class);
            query.setParameter("placa", placa);
            List<TbCasco> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
    public void crearCasco(TbCasco casco) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(casco);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
