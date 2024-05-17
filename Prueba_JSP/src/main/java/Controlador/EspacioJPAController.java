package Controlador;

import Modelo.TbEspacio;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.List;

public class EspacioJPAController implements Serializable {



    public EspacioJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }
    private EntityManagerFactory fabricaEntidades= null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

    public void create(TbEspacio espacio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(espacio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TbEspacio espacio) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            espacio = em.merge(espacio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = espacio.getId();
                if (findTbEspacio(id) == null) {
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
            TbEspacio espacio;
            try {
                espacio = em.getReference(TbEspacio.class, id);
                espacio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The espacio with id " + id + " no longer exists.", enfe);
            }
            em.remove(espacio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TbEspacio> findTbEspacioEntities() {
        return findTbEspacioEntities(true, -1, -1);
    }

    public List<TbEspacio> findTbEspacioEntities(int maxResults, int firstResult) {
        return findTbEspacioEntities(false, maxResults, firstResult);
    }

    private List<TbEspacio> findTbEspacioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TbEspacio.class));
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

    public TbEspacio findTbEspacio(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TbEspacio.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}

