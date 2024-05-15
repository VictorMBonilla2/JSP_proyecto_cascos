package Controlador;

import Modelo.TbCasillero;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class CasillerosJPAController implements Serializable {



    public CasillerosJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }
    private EntityManagerFactory fabricaEntidades=null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbCasillero casillero) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(casillero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbCasillero casillero) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            casillero = em.merge(casillero);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = casillero.getId();
                if (findTbCasillero(id) == null) {
                    throw new Exception("The casillero with id " + id + " no longer exists.");
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
            TbCasillero casillero;
            try {
                casillero = em.getReference(TbCasillero.class, id);
                casillero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The casillero with id " + id + " no longer exists.", enfe);
            }
            em.remove(casillero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbCasillero> findTbCasilleroEntities() {
        return findTbCasilleroEntities(true, -1, -1);
    }

    public List<TbCasillero> findTbCasilleroEntities(int maxResults, int firstResult) {
        return findTbCasilleroEntities(false, maxResults, firstResult);
    }

    private List<TbCasillero> findTbCasilleroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbCasillero.class));
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

    public TbCasillero findTbCasillero(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbCasillero.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

