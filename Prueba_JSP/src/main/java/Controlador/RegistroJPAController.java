package Controlador;

import Modelo.TbRegistro;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public class RegistroJPAController {

    public RegistroJPAController() {
        fabricaEntidades = Persistence.createEntityManagerFactory("default");
    }

    private EntityManagerFactory fabricaEntidades= null;

    public EntityManager getEntityManager() {
        return fabricaEntidades.createEntityManager();
    }

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
                int id = registro.getId_registro();
                if (findTbRegistro(id) == null) {
                    throw new Exception("The registro with id " + id + " no longer exists.");
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
            TbRegistro registro;
            try {
                registro = em.getReference(TbRegistro.class, id);
                registro.getId_registro();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The registro with id " + id + " no longer exists.", enfe);
            }
            em.remove(registro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public List<TbRegistro> findTbRegistroEntities() {
        return findTbRegistroEntities(true, -1, -1);
    }

    public List<TbRegistro> findTbRegistroEntities(int maxResults, int firstResult) {
        return findTbRegistroEntities(false, maxResults, firstResult);
    }
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
}
